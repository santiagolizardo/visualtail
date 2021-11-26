/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.menu;

import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;

import com.santiagolizardo.visualtail.gui.MainWindow;

public class Menu extends JMenuBar {


	private final FileMenu fileMenu;
	private final EditMenu editMenu;
	private final WindowsMenu windowMenu;

	public Menu(final JDesktopPane pane, MainWindow parentFrame) {

		fileMenu = new FileMenu(parentFrame);
		editMenu = new EditMenu(parentFrame);
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

	public FileMenu getFileMenu() {
		return fileMenu;
	}

	public EditMenu getEditMenu() {
		return editMenu;
	}

	public WindowsMenu getWindowMenu() {
		return windowMenu;
	}
}
