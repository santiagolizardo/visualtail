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

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.gui.actions.ActionFactory;
import com.santiagolizardo.beobachter.gui.actions.FindAction;
import com.santiagolizardo.beobachter.gui.actions.FindNextAction;

public class EditMenu extends JMenu {

	private static final long serialVersionUID = -8897022931984447153L;

	private JMenuItem copyMenuItem;

	public EditMenu(MainGUI mainGUI) {

		setText(_("Edit"));
		setMnemonic(KeyEvent.VK_E);

		ActionFactory actionFactory = mainGUI.getActionFactory();

		copyMenuItem = new JMenuItem(actionFactory.createCopyAction());
		JMenuItem selectAllMenuItem = new JMenuItem(
				actionFactory.createSelectAllAction());

		JMenuItem findMenuItem = new JMenuItem(new FindAction(mainGUI));
		JMenuItem findNextMenuItem = new JMenuItem(new FindNextAction(mainGUI));

		add(copyMenuItem);
		addSeparator();
		add(selectAllMenuItem);
		addSeparator();
		add(findMenuItem);
		add(findNextMenuItem);
	}
}
