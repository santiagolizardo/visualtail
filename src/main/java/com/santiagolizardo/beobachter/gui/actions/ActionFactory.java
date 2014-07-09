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
package com.santiagolizardo.beobachter.gui.actions;

import com.santiagolizardo.beobachter.gui.MainWindow;

public class ActionFactory {

	private OpenAction openAction;
	private CopyAction copyAction;
	private SelectAllAction selectAllAction;

	public ActionFactory(MainWindow mainWindow) {
		openAction = new OpenAction(mainWindow);
		copyAction = new CopyAction(mainWindow);
		selectAllAction = new SelectAllAction(mainWindow);
	}
	
	public OpenAction getOpenAction() {
		return openAction;
	}

	public CopyAction getCopyAction() {
		return copyAction;
	}

	public SelectAllAction getSelectAllAction() {
		return selectAllAction;
	}
}
