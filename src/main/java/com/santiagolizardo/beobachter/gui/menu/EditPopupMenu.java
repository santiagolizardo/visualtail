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
package com.santiagolizardo.beobachter.gui.menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.actions.ActionFactory;

public class EditPopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4858813412795446507L;

	public EditPopupMenu(MainWindow mainGUI) {

		ActionFactory actionFactory = mainGUI.getActionFactory();

		JMenuItem copy = new JMenuItem(actionFactory.createCopyAction());
		JMenuItem selectAll = new JMenuItem(
				actionFactory.createSelectAllAction());

		add(copy);
		addSeparator();
		add(selectAll);
	}
}
