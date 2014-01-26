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

	private CopyAction copyAction;
	private SelectAllAction selectAllAction;

	public ActionFactory(MainWindow mainGUI) {
		copyAction = new CopyAction(mainGUI);
		selectAllAction = new SelectAllAction(mainGUI);
	}

	public CopyAction createCopyAction() {
		return copyAction;
	}

	public SelectAllAction createSelectAllAction() {
		return selectAllAction;
	}
}
