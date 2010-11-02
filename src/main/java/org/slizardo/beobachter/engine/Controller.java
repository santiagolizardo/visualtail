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
package org.slizardo.beobachter.engine;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.configuration.ConfigurationException;
import org.slizardo.beobachter.Beobachter;
import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.config.ConfigManager;
import org.slizardo.beobachter.gui.dialogs.LogWindow;
import org.slizardo.beobachter.gui.util.DialogFactory;
import org.slizardo.beobachter.gui.util.EmptyIcon;
import org.slizardo.beobachter.gui.util.FileUtil;
import org.slizardo.beobachter.resources.languages.Translator;
import org.slizardo.beobachter.util.ArraysUtil;

public class Controller {

	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_FAILURE = 1;

	private static Logger logger = Logger.getLogger(Controller.class.getName());

	/**
	 * This method relocates all frames contained on the desktop and sets them
	 * on cascade.
	 */
	public static void setWindowsOnCascade(JDesktopPane desktop) {
		JInternalFrame[] frames = desktop.getAllFrames();
		if (frames.length == 0)
			return;

		short frameWidth = (short) ((desktop.getWidth() * 75) / 100);
		short frameHeight = (short) ((desktop.getHeight() * 75) / 100);

		short diffWidth = (short) (((desktop.getWidth() * 25) / 100) / frames.length);
		short diffHeight = (short) (((desktop.getHeight() * 25) / 100) / frames.length);

		short x = 0;
		short y = 0;

		for (byte i = 0; i < frames.length; i++) {
			try {
				frames[i].setMaximum(false);
				frames[i].setIcon(false);
			} catch (PropertyVetoException e) {
				logger.warning("Unable to set frame properties.");
			}

			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(x, y);

			x += diffWidth;
			y += diffHeight;
		}
	}

	/**
	 * This method relocates all frames contained on the desktop and sets them
	 * on vertical tile.
	 */
	public static void setWindowsOnTileVertical(JDesktopPane desktop) {
		JInternalFrame[] frames = desktop.getAllFrames();
		if (frames.length == 0)
			return;

		short frameWidth = (short) desktop.getWidth();
		short frameHeight = (short) (desktop.getHeight() / frames.length);

		for (short i = 0; i < frames.length; i++) {
			try {
				frames[i].setMaximum(false);
				frames[i].setIcon(false);
			} catch (PropertyVetoException e) {
				logger.warning("Unable to set frame properties.");
			}

			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(0, i * frameHeight);
		}
	}

	/**
	 * This method relocates all frames contained on the desktop and sets them
	 * on tile horizontal.
	 */
	public static void setWindowsOnTileHorizontal(JDesktopPane desktop) {
		JInternalFrame[] frames = desktop.getAllFrames();
		if (frames.length == 0)
			return;

		short frameWidth = (short) (desktop.getWidth() / frames.length);
		short frameHeight = (short) desktop.getHeight();

		for (short i = 0; i < frames.length; i++) {
			try {
				frames[i].setMaximum(false);
				frames[i].setIcon(false);
			} catch (PropertyVetoException e) {
				logger.warning("Unable to set frame properties.");
			}

			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(i * frameWidth, 0);
		}
	}

	/**
	 * This method terminates the application and saves all the runtime
	 * information on the filesystem.
	 */
	public static void exit() {
		int errCode = EXIT_FAILURE;

		try {
			Beobachter beobachter = Beobachter.instance;
			beobachter.setVisible(false);
			beobachter.dispose();

			ConfigManager config = beobachter.configManager;
			config.setWindowHeight(beobachter.getHeight());
			config.setWindowWidth(beobachter.getWidth());
			config.setWindowX(beobachter.getX());
			config.setWindowY(beobachter.getY());
			config.saveConfiguration();

			errCode = EXIT_SUCCESS;
		} catch (ConfigurationException e) {
			logger.severe(e.getMessage());
		}

		System.exit(errCode);
	}

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
						DialogFactory.showErrorMessage(Beobachter.instance, e
								.getMessage());
						return;
					}

					Controller.openFile(filePath);
				}
			});
			ArraysUtil.recents.add(fileName);
			ArraysUtil.recentsMenu.add(item);
		}
	}

	public static void initRecents() {
		JMenuItem cleanRecents = new JMenuItem(Translator.t("Clean_recents"));
		cleanRecents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Controller.cleanRecents();
			}
		});

		ArraysUtil.recentsMenu = new JMenu(Translator.t("Open_recents"));
		ArraysUtil.recentsMenu.setIcon(EmptyIcon.SIZE_16);
		ArraysUtil.recentsMenu.add(cleanRecents);
		ArraysUtil.recentsMenu.addSeparator();
		// Beobachter.instance.configManager.loadRecents();
	}

	public static void cleanRecents() {
		ArraysUtil.recents = new Vector<String>();
		int count = ArraysUtil.recentsMenu.getMenuComponentCount();
		for (int i = count - 1; i > 1; i--) {
			ArraysUtil.recentsMenu.remove(i);
		}
	}

	/**
	 * Invoked from "Recents" menu.
	 * 
	 * @param fileName
	 */
	public static void openFile(String fileName) {
		openFile(fileName, new LogType("Default"));
	}

	/**
	 * Invoked from the "Open" dialog.
	 * 
	 * @param fileName
	 * @param logType
	 */
	public static void openFile(String fileName, LogType logType) {
		LogWindow logWindow = new LogWindow(fileName, logType);
		Beobachter.instance.desktop.add(logWindow);
		try {
			logWindow.setSelected(true);
		} catch (PropertyVetoException pve) {
			pve.printStackTrace();
		}
	}
}
