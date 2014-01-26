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
package com.santiagolizardo.beobachter.gui.menu;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.gui.actions.ExitAction;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.gui.dialogs.SessionsDialog;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.gui.util.FileUtil;
import com.santiagolizardo.beobachter.resources.images.IconFactory;

public class FileMenu extends JMenu {

	private static final long serialVersionUID = -9095266179967845006L;

	private RecentsMenu recentsMenu;
	private JMenuItem saveSessionMenuItem;

	public FileMenu(final MainWindow mainGUI) {
		setText(_("File"));
		setMnemonic(KeyEvent.VK_F);

		JMenuItem open = new JMenuItem(_("Open..."));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK));
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				File lastSelected = new File(mainGUI.getConfigData().getLastPath());

				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(lastSelected);
				chooser.setMultiSelectionEnabled(true);
				int resp = chooser.showOpenDialog(mainGUI);
				if (resp == JFileChooser.APPROVE_OPTION) {
					File[] files = chooser.getSelectedFiles();
					LogType logType = new LogType("Default");

					List<File> unreadableFiles = new ArrayList<File>();

					for (File file : files) {

						try {
							FileUtil.tryReading(file);
						} catch (Exception ex) {
							unreadableFiles.add(file);
							continue;
						}

						Controller.openFile(mainGUI, file.getAbsolutePath(),
								logType);
						
						recentsMenu.addRecent(file.getAbsolutePath());
						recentsMenu.setEnabled(true);
					}

					mainGUI.desktop.setWindowsOnTileHorizontal();

					if (unreadableFiles.size() > 0) {
						StringBuffer message = new StringBuffer();
						message.append(_("These files could not be opened for reading:")
								+ "\n");
						for (File file : unreadableFiles)
							message.append("    - ")
									.append(file.getAbsolutePath())
									.append("\n");
						DialogFactory.showErrorMessage(mainGUI,
								message.toString());
					}
				}
			}
		});

		recentsMenu = new RecentsMenu(mainGUI);
		recentsMenu.setEnabled(!mainGUI.getConfigData().getRecentFiles().isEmpty());

		JMenuItem exit = new JMenuItem(new ExitAction(mainGUI));
		exit.setIcon(IconFactory.getImage("exit.png"));

		JMenuItem loadSession = new JMenuItem(_("Manage sessions..."));
		loadSession.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SessionsDialog dialog = new SessionsDialog(mainGUI, recentsMenu);
				dialog.setVisible(true);
			}
		});

		saveSessionMenuItem = new JMenuItem(_("Save current session"));
		saveSessionMenuItem.setIcon(IconFactory.getImage("disk.png"));
		saveSessionMenuItem.setEnabled(false);

		saveSessionMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JInternalFrame[] frames = mainGUI.desktop.getAllFrames();
				if (frames.length == 0) {
					DialogFactory
							.showErrorMessage(
									getParent(),
									_("You can not save a session with no opened files"));
					return;
				}
				String name = JOptionPane.showInputDialog(getParent()
						.getParent(), _("Please enter the session name:"),
						_("Session name"), JOptionPane.QUESTION_MESSAGE);
				if (name == null)
					return;

				name = name.trim();

				if (name.length() == 0) {
					DialogFactory.showErrorMessage(null,
							_("Invalid session name"));
					return;
				}

				try {
					File file = new File(Constants.FOLDER_SESSIONS
							+ File.separator + name + ".txt");
					FileWriter writer = new FileWriter(file);
					for (JInternalFrame frame : frames) {
						LogWindow window = (LogWindow) frame;
						writer.write(String.format("%s\n", window.getFile()
								.getAbsolutePath()));
					}
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		add(open);
		add(recentsMenu);
		addSeparator();
		add(loadSession);
		add(saveSessionMenuItem);
		addSeparator();
		add(exit);
	}

	public RecentsMenu getRecentsMenu() {
		return recentsMenu;
	}

	public JMenuItem getSaveSessionMenuItem() {
		return saveSessionMenuItem;
	}
}
