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
package com.santiagolizardo.beobachter.gui.actions;

import com.santiagolizardo.beobachter.MainGUI;

public class ActionFactory {

	private CopyAction copyAction;
	private SelectAllAction selectAllAction;

	public ActionFactory(MainGUI mainGUI) {
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
