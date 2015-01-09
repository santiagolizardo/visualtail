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

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.gui.util.EmptyIcon;
import java.awt.Toolkit;

public class SelectAllAction extends AbstractAction {

	private static final long serialVersionUID = 3272495186683089254L;

	private MainWindow mainWindow;

	public SelectAllAction(MainWindow mainWindow) {
		this.mainWindow = mainWindow;

		setEnabled(false);

		putValue(AbstractAction.SMALL_ICON, EmptyIcon.SIZE_16);
		putValue(AbstractAction.NAME, tr("Select all"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) mainWindow.getDesktop().getSelectedFrame();
		if (log != null) {
			int numLines = log.getLinesList().getModel().getSize();
			if (numLines > 0) {
				log.getLinesList().setSelectionInterval(0, numLines - 1);
			}
		}
	}
}
