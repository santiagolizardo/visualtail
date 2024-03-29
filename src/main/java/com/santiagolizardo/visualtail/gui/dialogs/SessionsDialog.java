/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs;

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.beans.Session;
import com.santiagolizardo.visualtail.gui.menu.RecentsMenu;
import com.santiagolizardo.visualtail.resources.languages.Translator;

/**
 * This class creates a window with the list of sessions available to open
 */
public class SessionsDialog extends AbstractDialog implements ActionListener {


	private final DefaultListModel<Session> listModel;
	private JList<Session> list;

	private JButton openButton;
	private JButton removeButton;

	private final RecentsMenu recentsMenu;

	private final MainWindow mainWindow;

	public SessionsDialog(MainWindow mainWindow, RecentsMenu recentsMenu) {
		super(mainWindow);

		setTitle(Translator.tr("Session management"));
		setModal(true);
		setSize(520, 320);

		this.mainWindow = mainWindow;

		this.recentsMenu = recentsMenu;

		listModel = new DefaultListModel<>();
		list = new JList<>(listModel);
		list.setCellRenderer(new SessionListCellRenderer());
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				if (!list.isSelectionEmpty() && ev.getClickCount() == 2) {
					openSession();
				}
			}
		});
		list.addListSelectionListener((ListSelectionEvent) -> {
			boolean enable = !list.isSelectionEmpty();
			openButton.setEnabled(enable);
			removeButton.setEnabled(enable);
		});

		openButton = new JButton(Translator.tr("Open"));
		openButton.setEnabled(false);
		openButton.addActionListener(this);

		removeButton = new JButton(Translator.tr("Remove"));
		removeButton.setEnabled(false);
		removeButton.addActionListener(this);

		placeComponents();

		updateList();
	}

	private void updateList() {
		listModel.clear();

		for (Session session : mainWindow.getConfigData().getSessions()) {
			listModel.addElement(session);
		}
	}

	private void placeComponents() {
		Container container = getContentPane();

		JPanel introPanel = new JPanel();
		JLabel introLabel = new JLabel(
				tr("Please select the session you want to open or remove:"));
		introPanel.add(introLabel);
		container.add(introPanel, BorderLayout.NORTH);

		JScrollPane scrollList = new JScrollPane(list);
		container.add(scrollList, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(openButton);
		panel.add(removeButton);
		container.add(panel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(openButton);

		setLocationRelativeTo(getOwner());
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (openButton == ev.getSource()) {
			openSession();
		} else if (removeButton == ev.getSource()) {
			removeSession();
		}
	}

	private void openSession() {
		setVisible(false);

		Session session = list.getSelectedValue();
		for (String fileName : session.getFileNames()) {
			mainWindow.getConfigData().getRecentFiles().remove(fileName);
			mainWindow.getConfigData().getRecentFiles().add(fileName);
			mainWindow.getActionFactory().getOpenAction().openFile(fileName, new LogType("Default"));
		}
		recentsMenu.refresh();

		mainWindow.getDesktop().setWindowsOnCascade();

		dispose();
	}

	private void removeSession() {
		Session session = list.getSelectedValue();
		int resp = JOptionPane.showConfirmDialog(getParent(),
				Translator.tr("Are you sure you want to delete this session?"));
		if (resp == JOptionPane.YES_OPTION) {
			mainWindow.getConfigData().getSessions().remove(session);
			updateList();
		}
	}
}

class SessionListCellRenderer extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		Session session = (Session) value;

		StringBuilder text = new StringBuilder();
		text.append("<html>");
		text.append(String.format("<strong>%s</strong> ", session.getName()));
		text.append(String.format(tr("%d file(s)"), session.getFileNames()
				.size()));
		text.append("<br />");
		session.getFileNames().stream().forEach((fileName) -> {
			text.append(String.format("- %s<br />", fileName));
		});
		text.append("</html>");

		label.setText(text.toString());

		return label;
	}
}
