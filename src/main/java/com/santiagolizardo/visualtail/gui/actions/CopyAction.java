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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.santiagolizardo.visualtail.Constants;
import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.resources.images.IconFactory;

public class CopyAction extends AbstractAction {

	private static final long serialVersionUID = 2226006120048433873L;

	private final MainWindow mainWindow;

	private final Clipboard clipboard;

	public CopyAction(MainWindow mainWindow) {

		this.mainWindow = mainWindow;

		putValue(AbstractAction.SMALL_ICON, IconFactory.getImage("copy.png"));
		putValue(AbstractAction.NAME, tr("Copy"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		setEnabled(false);
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) mainWindow.getDesktop().getSelectedFrame();
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
