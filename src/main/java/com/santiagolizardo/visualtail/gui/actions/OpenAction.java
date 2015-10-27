/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.actions;

import java.beans.PropertyVetoException;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import java.util.logging.Logger;

public class OpenAction {

	private static final Logger logger = Logger.getLogger(OpenAction.class.getName());
	
	private final MainWindow mainWindow;

	public OpenAction(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	/**
	 * Invoked from the "Open" dialog.
	 *
	 * @param fileName
	 * @param logType
	 */
	public void openFile(String fileName, LogType logType) {
		LogWindow logWindow = new LogWindow(mainWindow, fileName, logType);
		logWindow.setVisible(true);
		
		mainWindow.getDesktop().add(logWindow);
		
		try {
			logWindow.setSelected(true);
		} catch (PropertyVetoException pve) {
			logger.warning(pve.getMessage());
		}
	}
}
