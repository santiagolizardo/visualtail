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

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.Session;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.gui.menu.RecentsMenu;
import com.santiagolizardo.beobachter.resources.languages.Translator;

class SessionManager {

	public List<Session> getSessions() {
		List<Session> sessions = new ArrayList<>();

		File folder = new File(Constants.FOLDER_SESSIONS);
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".txt"));
			}
		};
		File[] files = folder.listFiles(filter);
		for (File file : files) {
			String name = file.getName().replaceAll("\\.txt", "");

			Session session = new Session();
			session.setName(name);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					session.getFileNames().add(line);
				}
				reader.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}

			sessions.add(session);
		}

		return sessions;
	}

	public boolean delete(String name) {
		File file = new File(Constants.FOLDER_SESSIONS + File.separator + name
				+ ".txt");
		return file.delete();
	}
}

/**
 * This class creates a window with the list of sessions available to open
 */
public class SessionsDialog extends AbstractDialog implements ActionListener {

	private static final long serialVersionUID = -8601498821660138035L;

	private DefaultListModel<Session> listModel;
	private JList<Session> list;

	private JButton btnOpen;
	private JButton btnRemove;

	private RecentsMenu recentsMenu;

	private SessionManager sessionManager;

	private MainGUI mainGUI;

	public SessionsDialog(MainGUI mainGUI, RecentsMenu recentsMenu) {
		super(mainGUI);

		setTitle(Translator._("Session management"));
		setModal(true);
		setSize(520, 320);

		this.mainGUI = mainGUI;

		sessionManager = new SessionManager();

		this.recentsMenu = recentsMenu;

		listModel = new DefaultListModel<Session>();
		list = new JList<Session>(listModel);
		list.setCellRenderer(new SessionListCellRenderer());
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!list.isSelectionEmpty() && e.getClickCount() == 2) {
					openSession();
				}
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean enable = !list.isSelectionEmpty();
				btnOpen.setEnabled(enable);
				btnRemove.setEnabled(enable);
			}
		});

		btnOpen = new JButton(Translator._("Open"));
		btnOpen.setEnabled(false);
		btnOpen.addActionListener(this);

		btnRemove = new JButton(Translator._("Remove"));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(this);

		placeComponents();

		updateList();
	}

	private void updateList() {
		listModel.clear();

		for (Session session : sessionManager.getSessions()) {
			listModel.addElement(session);
		}
	}

	private void placeComponents() {
		Container container = getContentPane();

		JPanel introPanel = new JPanel();
		JLabel introLabel = new JLabel(
				_("Please select the session you want to open or remove:"));
		introPanel.add(introLabel);
		container.add(introPanel, BorderLayout.NORTH);

		JScrollPane scrollList = new JScrollPane(list);
		container.add(scrollList, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(btnOpen);
		panel.add(btnRemove);
		container.add(panel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(btnOpen);

		setLocationRelativeTo(getOwner());
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (btnOpen == ev.getSource()) {
			openSession();
		} else if (btnRemove == ev.getSource()) {
			removeSession();
		}
	}

	private void openSession() {
		setVisible(false);

		Session session = list.getSelectedValue();
		for (String fileName : session.getFileNames()) {
			recentsMenu.addRecent(fileName);
			Controller.openFile(mainGUI, fileName, new LogType("Default"));
		}

		mainGUI.desktop.setWindowsOnCascade();

		dispose();
	}

	private void removeSession() {
		String sessionName = list.getSelectedValue().getName();
		int resp = JOptionPane.showConfirmDialog(getParent(),
				Translator._("Are you sure you want to delete this session?"));
		if (resp == JOptionPane.YES_OPTION) {
			sessionManager.delete(sessionName);
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

		StringBuffer text = new StringBuffer();
		text.append("<html>");
		text.append(String.format("<strong>%s</strong> ", session.getName()));
		text.append(String.format(_("%d file(s)"), session.getFileNames()
				.size()));
		text.append("<br />");
		for (String fileName : session.getFileNames()) {
			text.append(String.format("- %s<br />", fileName));
		}
		text.append("</html>");

		label.setText(text.toString());

		return label;
	}
}
