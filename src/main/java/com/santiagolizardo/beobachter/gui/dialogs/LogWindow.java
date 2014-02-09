/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Beobachter is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Beobachter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.dialogs;

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

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.engine.Tail;
import com.santiagolizardo.beobachter.engine.TailListener;
import com.santiagolizardo.beobachter.gui.adapters.LinesMouseAdapter;
import com.santiagolizardo.beobachter.gui.components.LogWindowToolbar;
import com.santiagolizardo.beobachter.gui.renderers.LineRenderer;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.util.FileUtil;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LogWindow extends JInternalFrame implements TailListener {

	private static final long serialVersionUID = -108911942974722973L;

	private int numberLinesToDisplay;

	private int searchIndex = 0;
	private String searchText = null;

	private File file;

	private Tail tail;

	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> task;

	private LogType logType;

	private JScrollPane scrollableList;

	private LogWindowToolbar toolbar;

	private DefaultListModel<String> linesModel;

	public JList<String> linesList;

	private MainWindow mainWindow;

	public LogWindow(final MainWindow mainWindow, String fileName, LogType logType) {

		setResizable(true);
		setFrameIcon(IconFactory.getImage("log_window.png"));

		numberLinesToDisplay = 256;

		this.mainWindow = mainWindow;
		this.logType = logType;

		mainWindow.getConfigData().setLastPath(fileName);
		mainWindow.updateActions(+1);

		linesModel = new DefaultListModel<>();
		linesList = new JList<>(linesModel);

		loadLogType(logType);

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

		tail = new Tail(fileName);
		tail.addListener(this);

		scheduler = Executors.newScheduledThreadPool(1);
		launchTask();

		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);

		defineLayout();

		setVisible(true);

		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				super.internalFrameActivated(e);

				mainWindow.setTitle(Constants.APP_NAME, file.getName());

				updateSelections();
			}

			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				super.internalFrameDeactivated(e);

				mainWindow.setTitle(Constants.APP_NAME);
			}

			@Override
			public void internalFrameClosing(InternalFrameEvent ev) {
				task.cancel(true);
				mainWindow.updateActions(-1);
			}
		});
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
			String line = linesModel.get(searchIndex).toString();
			if (line.indexOf(searchText) != -1) {
				linesList.ensureIndexIsVisible(searchIndex);
				linesList.setSelectedIndex(searchIndex);
				searchIndex++;
				break;
			}
		}
		if (searchIndex >= linesSize
				&& DialogFactory.showQuestionDialog(this,
						Translator._("Do you want to search again?"))) {
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
		String byteCount = FileUtil.byteCountToDisplaySize(file.length());
		setTitle(file.getName().concat(" - ").concat(byteCount));
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
		task = scheduler.scheduleAtFixedRate(tail, 0, logType.getRefreshInterval(), TimeUnit.MILLISECONDS);
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
	}

	public void loadLogType(LogType logType) {
		linesList.setCellRenderer(new LineRenderer(logType.getRules(),
				mainWindow.getConfigData()));
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
}
