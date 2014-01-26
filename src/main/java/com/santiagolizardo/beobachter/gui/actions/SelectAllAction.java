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
package com.santiagolizardo.beobachter.gui.actions;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.gui.util.EmptyIcon;

public class SelectAllAction extends AbstractAction {

	private static final long serialVersionUID = 3272495186683089254L;
	
	private MainWindow mainGUI;

	public SelectAllAction(MainWindow mainGUI) {
		this.mainGUI = mainGUI;

		setEnabled(false);
		
		putValue(AbstractAction.SMALL_ICON, EmptyIcon.SIZE_16);
		putValue(AbstractAction.NAME, _("Select all"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) mainGUI.desktop.getSelectedFrame();
		if (log != null) {
			int numLines = log.linesList.getModel().getSize();
			if (numLines > 0)
				log.linesList.setSelectionInterval(0, numLines - 1);
		}
	}
}
