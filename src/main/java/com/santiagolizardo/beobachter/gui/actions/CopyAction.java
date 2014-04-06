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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.resources.images.IconFactory;

public class CopyAction extends AbstractAction {

	private static final long serialVersionUID = 2226006120048433873L;

	private MainWindow mainGUI;

	private Clipboard clipboard;

	public CopyAction(MainWindow mainGUI) {

		this.mainGUI = mainGUI;

		putValue(AbstractAction.SMALL_ICON, IconFactory.getImage("copy.png"));
		putValue(AbstractAction.NAME, _("Copy"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));

		setEnabled(false);
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) mainGUI.getDesktop().getSelectedFrame();
		if (log != null) {
			List<String> selectedLines = log.getLinesList().getSelectedValuesList();
			StringBuilder sb = new StringBuilder();
			for (String selectedLine : selectedLines) {
				sb.append(selectedLine);
				sb.append(Constants.LINE_SEP);
			}
			clipboard
					.setContents(new StringSelection(sb.toString()), null /* ClipboardOwner */);
		}
	}
}
