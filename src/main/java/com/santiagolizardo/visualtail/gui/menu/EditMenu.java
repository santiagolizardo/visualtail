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

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.actions.ActionFactory;
import com.santiagolizardo.visualtail.gui.actions.ClearAllBuffersAction;
import com.santiagolizardo.visualtail.gui.actions.ClearSelectedBufferAction;
import com.santiagolizardo.visualtail.gui.actions.FindAction;
import com.santiagolizardo.visualtail.gui.actions.FindNextAction;
import com.santiagolizardo.visualtail.gui.actions.ReplaceAction;

public class EditMenu extends JMenu {


	private final JMenuItem copyMenuItem;
	private final JMenuItem clearSelectedWindowMenuItem;

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
		
		JMenuItem replaceMenuItem = new JMenuItem(new ReplaceAction(mainWindow));
		
		clearSelectedWindowMenuItem = new JMenuItem(new ClearSelectedBufferAction(mainWindow));
		JMenuItem clearAllBuffersMenuItem = new JMenuItem(new ClearAllBuffersAction(mainWindow));

		add(copyMenuItem);
		add(selectAllMenuItem);
		addSeparator();
		add(replaceMenuItem);		
		addSeparator();
		add(findMenuItem);
		add(findNextMenuItem);
		addSeparator();
		add(clearSelectedWindowMenuItem);
		add(clearAllBuffersMenuItem);
	}

	public JMenuItem getClearSelectedWindowMenuItem() {
		return clearSelectedWindowMenuItem;
	}
}
