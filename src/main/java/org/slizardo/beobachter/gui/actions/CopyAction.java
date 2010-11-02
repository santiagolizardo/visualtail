/**
 * Beobachter, the universal logs watcher
 * Copyright (C) 2009  Santiago Lizardo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

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

import org.slizardo.beobachter.Beobachter;
import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.gui.dialogs.LogWindow;
import org.slizardo.beobachter.resources.images.IconFactory;
import org.slizardo.beobachter.resources.languages.Translator;

class CopyAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2226006120048433873L;

	private StringBuffer buffer;

	private Clipboard clipboard;

	public CopyAction() {

		putValue(AbstractAction.SMALL_ICON, IconFactory.getImage("copy.png"));
		putValue(AbstractAction.NAME, Translator.t("Copy"));
		putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_C, KeyEvent.CTRL_MASK));

		buffer = new StringBuffer();
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) Beobachter.instance.desktop
				.getSelectedFrame();
		if (log != null) {
			Object[] selected = log.lines.getSelectedValues();
			buffer.setLength(0);
			for (int i = 0; i < selected.length; i++) {
				buffer.append(selected[i].toString());
				buffer.append(Constants.LINE_SEP);
			}
			clipboard
					.setContents(new StringSelection(buffer.toString()), null /* ClipboardOwner */);
		}
	}
}
