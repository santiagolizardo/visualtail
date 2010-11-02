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


import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.slizardo.beobachter.gui.actions.ActionFactory;
import org.slizardo.beobachter.gui.actions.FindAction;
import org.slizardo.beobachter.gui.actions.FindNextAction;
import org.slizardo.beobachter.resources.languages.Translator;

public class EditMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8897022931984447153L;

	public EditMenu() {

		setText(Translator.t("Edit"));
		setMnemonic(KeyEvent.VK_E);

		JMenuItem copy = new JMenuItem(ActionFactory.createCopyAction());
		JMenuItem selectAll = new JMenuItem(ActionFactory.createSelectAllAction());

		JMenuItem find = new JMenuItem(new FindAction());
		JMenuItem findNext = new JMenuItem(new FindNextAction());

		add(copy);
		addSeparator();
		add(selectAll);
		addSeparator();
		add(find);
		add(findNext);
	}
}
