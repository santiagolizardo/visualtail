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
package com.santiagolizardo.beobachter.engine;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.gui.util.EmptyIcon;
import com.santiagolizardo.beobachter.gui.util.FileUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.ArraysUtil;

public class Controller {

	private static Logger logger = Logger.getLogger(Controller.class.getName());

	public static void addRecent(String fileName) {
		if (!ArraysUtil.recents.contains(fileName)) {
			JMenuItem item = new JMenuItem(fileName);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					JMenuItem item = (JMenuItem) event.getSource();
					String filePath = item.getText();
					File file = new File(filePath);

					try {
						FileUtil.isReadable(file);
					} catch (Exception e) {
						DialogFactory.showErrorMessage(MainGUI.instance, e
								.getMessage());
						return;
					}

					Controller.openFile(filePath, new LogType("Default"));
				}
			});
			ArraysUtil.recents.add(fileName);
			ArraysUtil.recentsMenu.add(item);
		}
	}

	public static void initRecents() {
		JMenuItem cleanRecents = new JMenuItem(Translator._("Clean_recents"));
		cleanRecents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Controller.cleanRecents();
			}
		});

		ArraysUtil.recentsMenu = new JMenu(Translator._("Open_recents"));
		ArraysUtil.recentsMenu.setIcon(EmptyIcon.SIZE_16);
		ArraysUtil.recentsMenu.add(cleanRecents);
		ArraysUtil.recentsMenu.addSeparator();
		//MainGUI.instance.configManager.loadRecents();
	}

	public static void cleanRecents() {
		ArraysUtil.recents = new Vector<String>();
		int count = ArraysUtil.recentsMenu.getMenuComponentCount();
		for (int i = count - 1; i > 1; i--) {
			ArraysUtil.recentsMenu.remove(i);
		}
	}

	/**
	 * Invoked from the "Open" dialog.
	 * 
	 * @param fileName
	 * @param logType
	 */
	public static void openFile(String fileName, LogType logType) {
		LogWindow logWindow = new LogWindow(fileName, logType);
		MainGUI.instance.desktop.add(logWindow);
		try {
			logWindow.setSelected(true);
		} catch (PropertyVetoException pve) {
			pve.printStackTrace();
		}
	}
}
