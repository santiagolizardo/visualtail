/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.actions.ActionFactory;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContextualMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 4858813412795446507L;
	
	private MainWindow mainWindow;

	public ContextualMenu(MainWindow mainWindow) {

		this.mainWindow = mainWindow;
		
		ActionFactory actionFactory = mainWindow.getActionFactory();

		JMenuItem copy = new JMenuItem(actionFactory.getCopyAction());
		JMenuItem selectAll = new JMenuItem(
				actionFactory.getSelectAllAction());
		
		JMenuItem hideSelectedLinesMenuItem = new JMenuItem(Translator.tr("Hide selected lines"));
		hideSelectedLinesMenuItem.addActionListener(this);

		add(copy);
		add(selectAll);
		addSeparator();
		add(hideSelectedLinesMenuItem);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		LogWindow logWindow = (LogWindow)mainWindow.getDesktop().getSelectedFrame();
		logWindow.hideSelectedLines();
	}
}
