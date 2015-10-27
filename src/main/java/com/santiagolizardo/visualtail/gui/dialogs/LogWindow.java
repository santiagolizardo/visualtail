/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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
import com.santiagolizardo.visualtail.gui.dialogs.components.ReplacePanel;
import com.santiagolizardo.visualtail.gui.models.LogListModel;
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
import javax.swing.BoxLayout;
import javax.swing.event.InternalFrameAdapter;

public class LogWindow extends JInternalFrame implements TailListener {

	private static final long serialVersionUID = -108911942974722973L;

	private int numberPreviousLinesToDisplay;
	private int numberLinesToDisplay;

	private int searchIndex;
	private String searchText;
	private boolean searchCaseSensitive;

	private File file;

	private final TailNotifier tailNotifier;

	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> task;

	private final LogType logType;

	private final JScrollPane scrollableList;

	private LogWindowToolbar toolbar;

	private final LogListModel linesModel;

	private final JList<String> linesList;
	private final LineRenderer lineListRenderer;

	private final MainWindow mainWindow;

	private FindPanel findPanel;
	private ReplacePanel replacePanel;

	public LogWindow(final MainWindow mainWindow, String fileName, LogType logType) {

		setIconifiable(false);
		setResizable(true);
		// @todo Make frame icon shareable to all windows
		setFrameIcon(IconFactory.getImage("log_window.png"));
		setMaximizable(true);
		setClosable(true);

		searchIndex = 0;
		searchText = null;
		searchCaseSensitive = false;

		numberPreviousLinesToDisplay = 0;
		numberLinesToDisplay = 512;

		this.mainWindow = mainWindow;
		this.logType = logType;

		mainWindow.updateActions(+1);

		linesModel = new LogListModel();
		linesList = new JList<>(linesModel);

		lineListRenderer = new LineRenderer();
		lineListRenderer.loadLogType(logType);
		lineListRenderer.updateFont(mainWindow.getConfigData());
		linesList.setCellRenderer(lineListRenderer);

		linesList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		linesList.getSelectionModel().addListSelectionListener((ListSelectionEvent ev) -> {
			if (ev.getValueIsAdjusting() == false) {
				updateUiControls();
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
				updateUiControls();
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
		SwingUtilities.invokeLater(() -> {
			linesModel.addElement(line);
			trimLines();
			if (toolbar.getScrollNewLinesCheckBox().isSelected()) {
				moveToNewLines();
			}
			updateTitle();
			updateUiControls();
		});
	}

	private void defineLayout() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		contentPane.add(toolbar);
		contentPane.add(scrollableList);

		pack();
	}

	public void searchText(String searchText, boolean caseSensitive) {
		this.searchText = ( caseSensitive ? searchText : searchText.toLowerCase() );
		this.searchCaseSensitive = caseSensitive;
		
		int linesSize = linesModel.size();
		if( 0 == linesSize ) {
			return;
		}
		
		for (; searchIndex < linesSize; searchIndex++) {
			String line = linesModel.get(searchIndex);
			if( !caseSensitive ) {
				line = line.toLowerCase();
			}
			if (line.contains(this.searchText)) {
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
			searchText(searchText, searchCaseSensitive);
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

	private void updateUiControls() {
		boolean linesModelIsNotEmpty = !linesModel.isEmpty();
		mainWindow.getActionFactory().getSelectAllAction()
				.setEnabled(linesModelIsNotEmpty);
		mainWindow.getActionFactory().getCopyAction()
				.setEnabled(linesList.getSelectedValuesList().size() > 0);
		mainWindow.getMenu().getEditMenu().getClearSelectedWindowMenuItem().setEnabled(linesModelIsNotEmpty);
		toolbar.getClearButton().setEnabled(linesModelIsNotEmpty);
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
		updateUiControls();
	}

	/**
	 *
	 * @param numberOfLines
	 * @return Number of previous lines added.
	 */
	public int loadPreviousLines(int numberOfLines) {
		List<String> lines = tailNotifier.getTail().readPreviousLines(numberOfLines);
		for (String line : lines) {
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

	public void showFindPanel() {
		// Lazy initialization to save some memory if the panel
		// is never used.
		if (findPanel == null) {
			findPanel = new FindPanel(this);
		}

		getContentPane().add(findPanel);
		getContentPane().validate();

		findPanel.focus();
	}

	public void hideFindPanel() {
		getContentPane().remove(findPanel);
		getContentPane().validate();
	}

	public void showReplacePanel() {
		// Lazy initialization to save some memory if the panel
		// is never used.
		if (replacePanel == null) {
			replacePanel = new ReplacePanel(this);
		}

		getContentPane().add(replacePanel);
		getContentPane().validate();

		replacePanel.focus();
	}

	public void hideReplacePanel() {
		getContentPane().remove(replacePanel);
		getContentPane().validate();
	}

	public LineRenderer getLineRenderer() {
		return lineListRenderer;
	}

	public LogListModel getLinesModel() {
		return linesModel;
	}
}
