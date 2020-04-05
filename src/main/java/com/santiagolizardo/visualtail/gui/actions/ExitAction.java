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
package com.santiagolizardo.visualtail.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.resources.languages.Translator;

public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 9138179821476711088L;

	private final MainWindow mainWindow;

	public ExitAction(MainWindow mainGui) {

		this.mainWindow = mainGui;

		putValue(AbstractAction.NAME, Translator.tr("Exit"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		mainWindow.quit();
	}
}
