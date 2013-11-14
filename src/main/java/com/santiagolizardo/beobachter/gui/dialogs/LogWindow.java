/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2013 Santiago Lizardo (http://www.santiagolizardo.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.io.FileUtils;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.Rule;
import com.santiagolizardo.beobachter.engine.Tail;
import com.santiagolizardo.beobachter.engine.TailEvent;
import com.santiagolizardo.beobachter.engine.TailListener;
import com.santiagolizardo.beobachter.gui.menu.EditPopupMenu;
import com.santiagolizardo.beobachter.gui.renderers.LineRenderer;
import com.santiagolizardo.beobachter.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.ArraysUtil;

public class LogWindow extends JInternalFrame implements TailListener {

	private static final long serialVersionUID = -108911942974722973L;

	private File file;

	private int numberDisplayedLines;

	private JScrollPane scrollableList;

	private JToolBar toolbar;

	private DefaultListModel<String> linesModel;

	public JList<String> lines;

	private JCheckBox cbCheckChanges;
	private JCheckBox cbScrollNewLines;

	private JSpinner spNumberDisplayerLines;

	private JComboBox<LogType> logTypes;

	private Tail tail;

	private int searchIndex = 0;
	private String searchText = null;

	public LogWindow(String fileName, LogType logType) {

		setResizable(true);
		setFrameIcon(IconFactory.getImage("log_window.png"));

		MainGUI.instance.configData.setLastPath(fileName);

		numberDisplayedLines = 64;

		spNumberDisplayerLines = new JSpinner(new SpinnerNumberModel(
				numberDisplayedLines, 1, 999, 1));
		spNumberDisplayerLines.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				numberDisplayedLines = (int) spNumberDisplayerLines.getValue();
				trimLines();
			}
		});

		linesModel = new DefaultListModel<String>();
		lines = new JList<String>(linesModel);
		lines.setCellRenderer(new LineRenderer());
		if (logType != null) {
			setRendererRules(logType.getRules());
		}
		lines.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lines.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent event) {
				if (event.isPopupTrigger()) {
					EditPopupMenu popupMenu = new EditPopupMenu();
					popupMenu.show((JComponent) event.getSource(),
							event.getX(), event.getY());
				}
			}
		});
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent event) {
				tail.setEnabled(false);
			}
		});

		scrollableList = new JScrollPane(lines);
		Dimension dim = new Dimension(320, 200);
		scrollableList.setSize(dim);
		scrollableList.setPreferredSize(dim);
		scrollableList.getVerticalScrollBar().addMouseListener(
				new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						cbScrollNewLines.setSelected(false);
					}
				});

		toolbar = new JToolBar();
		toolbar.setFloatable(false);

		cbCheckChanges = new JCheckBox(Translator._("Check for changes"));
		cbCheckChanges.setSelected(true);
		cbCheckChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean selected = cbCheckChanges.isSelected();
				tail.setEnabled(selected);
				if (selected) {
					new Thread(tail).start();
				}
			}
		});

		cbScrollNewLines = new JCheckBox(Translator._("Scroll to new lines"));

		DefaultComboBoxModel<LogType> logTypesModel = new DefaultComboBoxModel<LogType>(
				ArraysUtil.arrayLogTypes());
		logTypes = new JComboBox<LogType>(logTypesModel);
		logTypes.setRenderer(new LogTypeListRenderer());
		logTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LogType logType = (LogType) logTypes.getSelectedItem();
				setRendererRules(logType.getRules());
			}
		});
		logTypes.setSelectedItem(logType);

		JButton btnClear = new JButton(Translator._("Clear_buffer"));
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				linesModel.clear();
			}
		});

		toolbar.add(cbCheckChanges);
		toolbar.add(cbScrollNewLines);
		toolbar.add(logTypes);
		toolbar.add(spNumberDisplayerLines);
		addPrintMenuItem();
		toolbar.add(btnClear);

		file = new File(fileName);

		updateTitle();

		tail = new Tail(fileName, logType.getRefreshInterval());
		tail.addListener(this);
		new Thread(tail).start();

		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);

		defineLayout();

		setVisible(true);
	}

	/**
	 * Tries to add the print menu if the desktop supports it.
	 */
	private void addPrintMenuItem() {
		if (Desktop.isDesktopSupported()) {
			final Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Action.PRINT)) {

				JButton btnPrint = new JButton(Translator._("Print_this_file"));
				btnPrint.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							desktop.print(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

				toolbar.add(btnPrint);
			}
		}

	}

	public void onFileChanges(final TailEvent tailEvent) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				linesModel.addElement(tailEvent.getLine());
				trimLines();
				if (cbScrollNewLines.isSelected()) {
					int lastIndex = linesModel.size() - 1;
					if (lastIndex >= 0) {
						lines.ensureIndexIsVisible(lastIndex);
					}
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
				lines.ensureIndexIsVisible(searchIndex);
				lines.setSelectedIndex(searchIndex);
				searchIndex++;
				break;
			}
		}
		if (searchIndex >= linesSize
				&& DialogFactory.showQuestionDialog(this,
						Translator._("Do_you_want_to_search_again"))) {
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
		String byteCount = FileUtils.byteCountToDisplaySize(file.length());
		setTitle(file.getName().concat(" - ").concat(byteCount));
	}

	public void setRendererRules(List<Rule> rules) {
		LineRenderer lineRenderer = (LineRenderer) lines.getCellRenderer();
		lineRenderer.setRules(rules);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	private void trimLines() {
		int numLines = linesModel.size();
		if (numLines > numberDisplayedLines) {
			linesModel.removeRange(0, numLines - numberDisplayedLines - 1);
		}
	}
}
