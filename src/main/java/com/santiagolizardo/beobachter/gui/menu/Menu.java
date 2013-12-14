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
package com.santiagolizardo.beobachter.gui.menu;

import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;

import com.santiagolizardo.beobachter.MainGUI;

public class Menu extends JMenuBar {

	private static final long serialVersionUID = -1490576137341648743L;

	private WindowsMenu windowMenu;
	
	public Menu(final JDesktopPane pane, MainGUI parentFrame) {

		FileMenu fileMenu = new FileMenu(parentFrame);
		EditMenu editMenu = new EditMenu(parentFrame);
		OptionsMenu optionsMenu = new OptionsMenu(parentFrame);
		
		windowMenu = new WindowsMenu(parentFrame);
		windowMenu.setEnabled(false);
		
		HelpMenu helpMenu = new HelpMenu(parentFrame);

		add(fileMenu);
		add(editMenu);
		add(optionsMenu);
		add(windowMenu);
		add(Box.createHorizontalGlue());
		add(helpMenu);
	}
	
	public WindowsMenu getWindowMenu() {
		return windowMenu;
	}
}
