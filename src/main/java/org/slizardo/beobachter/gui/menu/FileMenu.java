/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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
package org.slizardo.beobachter.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.engine.Controller;
import org.slizardo.beobachter.gui.actions.ExitAction;
import org.slizardo.beobachter.gui.dialogs.LogWindow;
import org.slizardo.beobachter.gui.dialogs.OpenFileDialog;
import org.slizardo.beobachter.gui.dialogs.SessionsDialog;
import org.slizardo.beobachter.gui.util.DialogFactory;
import org.slizardo.beobachter.resources.images.IconFactory;
import org.slizardo.beobachter.resources.languages.Translator;
import org.slizardo.beobachter.util.ArraysUtil;

public class FileMenu extends JMenu {

	private static final long serialVersionUID = -9095266179967845006L;

	public FileMenu() {
		setText(Translator.t("File"));
		setMnemonic(KeyEvent.VK_F);

		JMenuItem open = new JMenuItem(Translator.t("Open..."));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK));
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpenFileDialog ofDialog = new OpenFileDialog();
				ofDialog.setVisible(true);
			}
		});

		Controller.initRecents();

		JMenuItem exit = new JMenuItem(new ExitAction());
		exit.setIcon(IconFactory.getImage("exit.png"));

		JMenuItem loadSession = new JMenuItem(Translator.t("Load_session..."));
		loadSession.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SessionsDialog dialog = new SessionsDialog();
				dialog.setVisible(true);
			}
		});

		JMenuItem saveSession = new JMenuItem(
				Translator.t("Save_current_session"));
		saveSession.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JInternalFrame[] frames = MainGUI.instance.desktop
						.getAllFrames();
				if (frames.length == 0) {
					DialogFactory.showErrorMessage(getParent(),
							"You can not save a session with no opened files");
					return;
				}
				String name = JOptionPane.showInputDialog(getParent()
						.getParent(), "Please enter the session name:");
				if (name == null)
					return;

				name = name.trim();

				if (name.length() > 0) {
					try {
						File file = new File(Constants.FOLDER_SESSIONS
								+ Constants.DIR_SEP + name + ".txt");
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
			}
		});

		add(open);
		add(ArraysUtil.recentsMenu);
		addSeparator();
		add(loadSession);
		add(saveSession);
		addSeparator();
		add(exit);
	}
}
