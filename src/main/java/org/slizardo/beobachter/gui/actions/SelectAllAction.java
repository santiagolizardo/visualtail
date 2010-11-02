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


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.slizardo.beobachter.Beobachter;
import org.slizardo.beobachter.gui.dialogs.LogWindow;
import org.slizardo.beobachter.gui.util.EmptyIcon;
import org.slizardo.beobachter.resources.languages.Translator;

class SelectAllAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3272495186683089254L;

	public SelectAllAction() {

		putValue(AbstractAction.SMALL_ICON, EmptyIcon.SIZE_16);
		putValue(AbstractAction.NAME, Translator.t("Select_all"));
		putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_A, KeyEvent.CTRL_MASK));
	}

	public void actionPerformed(ActionEvent event) {
		LogWindow log = (LogWindow) Beobachter.instance.desktop
				.getSelectedFrame();
		if (log != null) {
			int numLines = log.lines.getModel().getSize();
			if(numLines > 0)
				log.lines.setSelectionInterval(0, numLines - 1);
		}
	}
}
