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

import static com.santiagolizardo.beobachter.resources.languages.Translator.tr;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.actions.ActionFactory;
import com.santiagolizardo.beobachter.gui.actions.ClearAllBuffersAction;
import com.santiagolizardo.beobachter.gui.actions.ClearSelectedBufferAction;
import com.santiagolizardo.beobachter.gui.actions.FindAction;
import com.santiagolizardo.beobachter.gui.actions.FindNextAction;

public class EditMenu extends JMenu {

	private static final long serialVersionUID = -8897022931984447153L;

	private JMenuItem copyMenuItem;

	public EditMenu(MainWindow mainWindow) {

		setText(tr("Edit"));
		setEnabled(false);
		setMnemonic(KeyEvent.VK_E);

		ActionFactory actionFactory = mainWindow.getActionFactory();

		copyMenuItem = new JMenuItem(actionFactory.getCopyAction());
		JMenuItem selectAllMenuItem = new JMenuItem(
				actionFactory.getSelectAllAction());

		JMenuItem findMenuItem = new JMenuItem(new FindAction(mainWindow));
		JMenuItem findNextMenuItem = new JMenuItem(new FindNextAction(mainWindow));
		
		JMenuItem clearSelectedBufferMenuItem = new JMenuItem(new ClearSelectedBufferAction(mainWindow));
		JMenuItem clearAllBuffersMenuItem = new JMenuItem(new ClearAllBuffersAction(mainWindow));

		add(copyMenuItem);
		addSeparator();
		add(selectAllMenuItem);
		addSeparator();
		add(findMenuItem);
		add(findNextMenuItem);
		addSeparator();
		add(clearSelectedBufferMenuItem);
		add(clearAllBuffersMenuItem);
	}
}
