/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.santiagolizardo.visualtail.Constants;
import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.engine.TailListener;
import com.santiagolizardo.visualtail.engine.TailNotifier;
import com.santiagolizardo.visualtail.gui.adapters.LinesMouseAdapter;
import com.santiagolizardo.visualtail.gui.components.LogWindowToolbar;
import com.santiagolizardo.visualtail.gui.dialogs.components.FindPanel;
import com.santiagolizardo.visualtail.gui.renderers.LineRenderer;
import com.santiagolizardo.visualtail.gui.util.DialogFactory;
import com.santiagolizardo.visualtail.util.FileUtil;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import java.awt.Font;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LogWindow extends JInternalFrame implements TailListener {

	private static final long serialVersionUID = -108911942974722973L;

	private int numberPreviousLinesToDisplay;
	private int numberLinesToDisplay;

	private int searchIndex;
	private String searchText;

	private File file;

	private TailNotifier tailNotifier;

	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> task;

	private LogType logType;

	private JScrollPane scrollableList;

	private LogWindowToolbar toolbar;

	private DefaultListModel<String> linesModel;

	private JList<String> linesList;
	private LineRenderer lineListRenderer;

	private MainWindow mainWindow;

	private FindPanel findPanel;
	
	public LogWindow(final MainWindow mainWindow, String fileName, LogType logType) {

		setIconifiable(false);
		setResizable(true);
        // @todo Make frame icon shareable to all windows
		setFrameIcon(IconFactory.getImage("log_window.png"));
		setMaximizable(true);
		setClosable(true);

		searchIndex = 0;
		searchText = null;

		numberPreviousLinesToDisplay = 0;
		numberLinesToDisplay = 512;

		this.mainWindow = mainWindow;
		this.logType = logType;

		mainWindow.getConfigData().setLastPath(fileName);
		mainWindow.updateActions(+1);

		linesModel = new DefaultListModel<>();
		linesList = new JList<>(linesModel);

		lineListRenderer = new LineRenderer();
		lineListRenderer.loadLogType(logType);
		lineListRenderer.updateFont(mainWindow.getConfigData());
		linesList.setCellRenderer(lineListRenderer);

		linesList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		linesList.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent ev) {
						if (ev.getValueIsAdjusting() == false) {
							updateSelections();
						}
					}
				});
		linesList.addMouseListener(new LinesMouseAdapter(mainWindow));

		scrollableList = new JScrollPane(linesList);
		Dimension dim = new Dimension(320, 200);
		scrollableList.setSize(dim);
		scrollableList.setPreferredSize(dim);
		scrollableList.getVerticalScrollBar().addMouseListener(
				new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						toolbar.getScrollNewLinesCheckBox().setSelected(false);
					}
				});

		toolbar = new LogWindowToolbar(this, logType);

		file = new File(fileName);

		updateTitle();

		tailNotifier = new TailNotifier(fileName);
		tailNotifier.addListener(this);
		tailNotifier.addListener(toolbar);

		scheduler = Executors.newScheduledThreadPool(1);
		launchTask();

		defineLayout();

		setVisible(true);

		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent ev) {
				mainWindow.setTitle(Constants.APP_NAME, file.getName());
				updateSelections();
			}

			@Override
			public void internalFrameDeactivated(InternalFrameEvent ev) {
				mainWindow.setTitle(Constants.APP_NAME);
			}

			@Override
			public void internalFrameClosing(InternalFrameEvent ev) {
				task.cancel(true);
				scheduler.shutdown();
				mainWindow.updateActions(-1);
			}
		});
	}

	public void loadLogType(LogType logType) {
		lineListRenderer.loadLogType(logType);
		linesList.updateUI();
	}
	
	public void updateFont(Font font) {
		lineListRenderer.setFont(font);
		linesList.updateUI();
	}

	@Override
	public void onFileChanges(final String line) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				linesModel.addElement(line);
				trimLines();
				if (toolbar.getScrollNewLinesCheckBox().isSelected()) {
					moveToNewLines();
				}
				updateTitle();
			}
		});
	}

	private void defineLayout() {
		Container contentPane = getContentPane();

		contentPane.add(toolbar, BorderLayout.NORTH);
		contentPane.add(scrollableList, BorderLayout.CENTER);

		pack();
	}

	public void searchText(String searchText) {
		this.searchText = searchText;
		int linesSize = linesModel.size();
		for (; searchIndex < linesSize; searchIndex++) {
			String line = linesModel.get(searchIndex);
			if (line.contains(searchText) ) {
				linesList.ensureIndexIsVisible(searchIndex);
				linesList.setSelectedIndex(searchIndex);
				searchIndex++;
				break;
			}
		}
		if (searchIndex >= linesSize
				&& DialogFactory.showQuestionDialog(this,
						Translator.tr("Do you want to search again?"))) {
			searchIndex = 0;
			searchAgainText();
		}
	}

	public void searchAgainText() {
		if (searchText != null) {
			searchText(searchText);
		}
	}

	private void updateTitle() {
		String newTitle = file.getName();

		String byteCount = FileUtil.byteCountToDisplaySize(file.length());
		newTitle = newTitle.concat(" - ").concat(byteCount);

		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, getLocale());
		String modDate = dateFormat.format(file.lastModified());
		newTitle = newTitle.concat(" - ").concat(modDate);

		setTitle(newTitle);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void trimLines() {
		int numLines = linesModel.size();
		if (numLines > numberLinesToDisplay) {
			linesModel.removeRange(0, numLines - numberLinesToDisplay - 1);
		}
	}

	private void updateSelections() {
		mainWindow.getActionFactory().getCopyAction()
				.setEnabled(linesList.getSelectedValuesList().size() > 0);
	}

	private void launchTask() {
		task = scheduler.scheduleAtFixedRate(tailNotifier, 0, logType.getRefreshInterval(), TimeUnit.MILLISECONDS);
	}

	public void hideSelectedLines() {
		int[] selectedIndexes = linesList.getSelectedIndices();
		for (int i = selectedIndexes.length - 1; i >= 0; i--) {
			linesModel.remove(selectedIndexes[i]);
		}
	}

	public void checkForChanges() {
		task.cancel(true);
		if (toolbar.getCheckForChangesCheckBox().isSelected()) {
			launchTask();
		}
	}

	public void clear() {
		linesModel.clear();
		toolbar.getClearButton().setEnabled(false);
	}

	/**
	 * 
	 * @param numberOfLines
	 * @return Number of previous lines added.
	 */
	public int loadPreviousLines(int numberOfLines) {
            List<String> lines = tailNotifier.getTail().readPreviousLines(numberOfLines);
            for(String line : lines ) {
                linesModel.add(0, line);
            }
			return lines.size();
	}
	
	public int getNumberPreviousLinesToDisplay() {
		return numberPreviousLinesToDisplay;
	}

	public void setNumberPreviousLinesToDisplay(int numberPreviousLinesToDisplay) {
		this.numberPreviousLinesToDisplay = numberPreviousLinesToDisplay;
	}

	public int getNumberLinesToDisplay() {
		return numberLinesToDisplay;
	}

	public void setNumberLinesToDisplay(int numberLinesToDisplay) {
		this.numberLinesToDisplay = numberLinesToDisplay;
	}

	public void moveToNewLines() {
		int lastIndex = linesModel.size() - 1;
		if (lastIndex >= 0) {
			linesList.ensureIndexIsVisible(lastIndex);
		}
	}

	public JList<String> getLinesList() {
		return linesList;
	}
	
	public void addFindPanel() {
		if (findPanel == null) {
			findPanel = new FindPanel(this);

			getContentPane().add(findPanel, BorderLayout.PAGE_END);
			getContentPane().validate();
		}
		
		findPanel.focus();		
	}

	public void removeFindPanel() {
		getContentPane().remove(findPanel);
		getContentPane().validate();

		findPanel = null;
	}
}
