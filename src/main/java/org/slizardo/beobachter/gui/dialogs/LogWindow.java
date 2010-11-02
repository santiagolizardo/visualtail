/**
 * Beobachter, the universal logs watcher
 * Copyright (C) 2009  Santiago Lizardo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.slizardo.beobachter.gui.dialogs;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Desktop.Action;
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
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.io.FileUtils;
import org.slizardo.beobachter.Beobachter;
import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.beans.Rule;
import org.slizardo.beobachter.engine.Tail;
import org.slizardo.beobachter.engine.TailEvent;
import org.slizardo.beobachter.engine.TailListener;
import org.slizardo.beobachter.gui.menu.EditPopupMenu;
import org.slizardo.beobachter.gui.renderers.LineRenderer;
import org.slizardo.beobachter.gui.renderers.LogTypeListRenderer;
import org.slizardo.beobachter.gui.util.DialogFactory;
import org.slizardo.beobachter.resources.images.IconFactory;
import org.slizardo.beobachter.resources.languages.Translator;
import org.slizardo.beobachter.util.ArraysUtil;

public class LogWindow extends JInternalFrame implements TailListener {

	private static final long serialVersionUID = -108911942974722973L;

	private static final int MAX_BUFFER = 128;

	private File file;

	private JScrollPane scrollableList;

	private JToolBar toolbar;

	private DefaultListModel linesModel;

	public JList lines;

	private JCheckBox followTail;

	private JComboBox logTypes;

	private Tail tail;

	private int searchIndex = 0;
	private String searchText = null;

	public LogWindow(String fileName, LogType logType) {

		setResizable(true);
		setFrameIcon(IconFactory.getImage("log_window.png"));

		Beobachter.instance.configManager.setLastPath(fileName);

		linesModel = new DefaultListModel();
		lines = new JList(linesModel);
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

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		followTail = new JCheckBox(Translator.t("Follow_tail"));

		DefaultComboBoxModel logTypesModel = new DefaultComboBoxModel(
				ArraysUtil.arrayLogTypes());
		logTypes = new JComboBox(logTypesModel);
		logTypes.setRenderer(new LogTypeListRenderer());
		logTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox combo = (JComboBox) event.getSource();
				LogType logType = (LogType) combo.getSelectedItem();
				setRendererRules(logType.getRules());
			}
		});
		logTypes.setSelectedItem(logType);

		final Desktop desktop = Desktop.getDesktop();

		JButton btnPrint = new JButton(Translator.t("Print_this_file"));
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

		JButton btnClear = new JButton(Translator.t("Clear_buffer"));
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				linesModel.clear();
			}
		});

		toolbar.add(followTail);
		toolbar.add(logTypes);
		if (desktop.isSupported(Action.PRINT)) {
			toolbar.add(btnPrint);
		}
		toolbar.add(btnClear);

		file = new File(fileName);

		updateTitle();

		tail = new Tail(fileName, logType.getRefreshInterval());
		tail.addListener(this);
		tail.start();

		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);

		defineLayout();

		setVisible(true);
	}

	public void onFileChanges(final TailEvent tailEvent) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				linesModel.addElement(tailEvent.getLine());
				if (linesModel.size() > MAX_BUFFER)
					linesModel.remove(0);
				if (followTail.isSelected()) {
					int lastIndex = linesModel.getSize() - 1;
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
				&& DialogFactory.showQuestionDialog(this, Translator
						.t("Do_you_want_to_search_again"))) {
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

}
