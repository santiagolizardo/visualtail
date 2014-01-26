/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beobachter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beobachter.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.engine;

import java.beans.PropertyVetoException;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import java.util.logging.Logger;

public class Controller {
	
	private static final Logger logger = Logger.getLogger(Controller.class.getName());

	/**
	 * Invoked from the "Open" dialog.
	 * 
	 * @param mainGUI
	 * @param fileName
	 * @param logType
	 */
	public static void openFile(MainWindow mainGUI, String fileName, LogType logType) {
		LogWindow logWindow = new LogWindow(mainGUI, fileName, logType);
		mainGUI.desktop.add(logWindow);
		try {
			logWindow.setSelected(true);
		} catch (PropertyVetoException pve) {
			logger.warning(pve.getMessage());
		}
	}
}
