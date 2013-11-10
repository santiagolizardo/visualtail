/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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
package com.santiagolizardo.beobachter.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

class CopyAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2226006120048433873L;

	private Clipboard clipboard;

	public CopyAction() {

		putValue(AbstractAction.SMALL_ICON, IconFactory.getImage("copy.png"));
		putValue(AbstractAction.NAME, Translator._("Copy"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) MainGUI.instance.desktop.getSelectedFrame();
		if (log != null) {
			List<String> selectedLines = log.lines.getSelectedValuesList();
			StringBuilder sb = new StringBuilder();
			for (String selectedLine : selectedLines ) {
				sb.append(selectedLine);
				sb.append(Constants.LINE_SEP);
			}
			clipboard
					.setContents(new StringSelection(sb.toString()), null /* ClipboardOwner */);
		}
	}
}
