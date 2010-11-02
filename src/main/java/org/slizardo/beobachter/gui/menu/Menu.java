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
package org.slizardo.beobachter.gui.menu;

import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;

public class Menu extends JMenuBar {

	private static final long serialVersionUID = -1490576137341648743L;

	public Menu(final JDesktopPane pane) {

		FileMenu fileMenu = new FileMenu();
		EditMenu editMenu = new EditMenu();
		OptionsMenu optionsMenu = new OptionsMenu();
		WindowsMenu windowMenu = new WindowsMenu();
		HelpMenu helpMenu = new HelpMenu();

		add(fileMenu);
		add(editMenu);
		add(optionsMenu);
		add(windowMenu);
		add(Box.createHorizontalGlue());
		add(helpMenu);
	}
}
