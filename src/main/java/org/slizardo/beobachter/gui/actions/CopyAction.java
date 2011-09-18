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
package org.slizardo.beobachter.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.gui.dialogs.LogWindow;
import org.slizardo.beobachter.resources.images.IconFactory;
import org.slizardo.beobachter.resources.languages.Translator;

class CopyAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2226006120048433873L;

	private Clipboard clipboard;

	public CopyAction() {

		putValue(AbstractAction.SMALL_ICON, IconFactory.getImage("copy.png"));
		putValue(AbstractAction.NAME, Translator.t("Copy"));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) MainGUI.instance.desktop.getSelectedFrame();
		if (log != null) {
			Object[] selected = log.lines.getSelectedValues();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < selected.length; i++) {
				sb.append(selected[i].toString());
				sb.append(Constants.LINE_SEP);
			}
			clipboard
					.setContents(new StringSelection(sb.toString()), null /* ClipboardOwner */);
		}
	}
}
