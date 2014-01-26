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

import java.beans.PropertyVetoException;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;

public class Controller {

	/**
	 * Invoked from the "Open" dialog.
	 * 
	 * @param fileName
	 * @param logType
	 */
	public static void openFile(MainWindow mainGUI, String fileName, LogType logType) {
		LogWindow logWindow = new LogWindow(mainGUI, fileName, logType);
		mainGUI.desktop.add(logWindow);
		try {
			logWindow.setSelected(true);
		} catch (PropertyVetoException pve) {
			pve.printStackTrace();
		}
	}
}
