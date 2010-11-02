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

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.engine.Controller;
import org.slizardo.beobachter.resources.languages.Translator;

/**
 * This class creates a window with the list of sessions available to open
 * 
 * @author slizardo
 * 
 */
public class SessionsDialog extends JDialog {

	private static final long serialVersionUID = -8601498821660138035L;

	private DefaultListModel listModel;
	private JList list;

	private JButton btnOpen;
	private JButton btnRemove;

	public SessionsDialog() {
		setTitle(Translator.t("Sessions"));
		setModal(true);
		setSize(320, 240);
		setLocationRelativeTo(null);

		listModel = new DefaultListModel();
		list = new JList(listModel);
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

		btnOpen = new JButton(Translator.t("Open"));
		btnOpen.setEnabled(false);
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openSession();
			}
		});

		btnRemove = new JButton(Translator.t("Remove"));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sessionName = list.getSelectedValue().toString();
				int resp = JOptionPane.showConfirmDialog(getParent(),
						Translator.t("Are you sure you want to delete this session?"));
				if (resp == JOptionPane.YES_OPTION) {
					File file = new File(Constants.FOLDER_SESSIONS + "/"
							+ sessionName + ".txt");
					file.delete();
				}
				updateList();
			}
		});

		placeComponents();

		updateList();
	}

	private void updateList() {
		listModel.clear();

		File folder = new File(Constants.FOLDER_SESSIONS);
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".txt"));
			}
		};
		File[] files = folder.listFiles(filter);
		for (File file : files) {
			listModel.addElement(file.getName().replaceAll("\\.txt", ""));
		}
	}

	private void placeComponents() {
		SpringLayout spring = new SpringLayout();

		JLabel lblList = new JLabel("Session list");

		JScrollPane scrollList = new JScrollPane(list);
		scrollList.setPreferredSize(new Dimension(160, 170));

		Container container = getContentPane();
		container.setLayout(spring);

		spring.putConstraint(NORTH, lblList, 5, WEST, container);
		spring.putConstraint(NORTH, scrollList, 5, SOUTH, lblList);
		spring.putConstraint(NORTH, btnOpen, 5, SOUTH, lblList);
		spring.putConstraint(NORTH, btnRemove, 5, SOUTH, btnOpen);

		spring.putConstraint(WEST, lblList, 5, WEST, container);
		spring.putConstraint(WEST, scrollList, 5, WEST, container);
		spring.putConstraint(WEST, btnOpen, 5, EAST, scrollList);
		spring.putConstraint(EAST, btnOpen, -5, EAST, container);
		spring.putConstraint(WEST, btnRemove, 5, EAST, scrollList);
		spring.putConstraint(EAST, btnRemove, -5, EAST, container);

		container.add(lblList);
		container.add(scrollList);
		container.add(btnOpen);
		container.add(btnRemove);
	}

	private void openSession() {
		String path = list.getSelectedValue().toString().concat(".txt");
		File file = new File(Constants.FOLDER_SESSIONS + Constants.DIR_SEP
				+ path);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				Controller.openFile(line, new LogType("Default"));
			}
			reader.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		Controller.setWindowsOnCascade(MainGUI.instance.desktop);

		setVisible(false);
		dispose();
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();

		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		return rootPane;
	}
}
