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
import com.santiagolizardo.beobachter.resources.images.IconFactory;

public class FindAction extends AbstractAction {

	private static final long serialVersionUID = 2244429466145757856L;

	private MainWindow mainGUI;

	public FindAction(MainWindow mainGUI) {
		
		this.mainGUI = mainGUI;

		putValue(AbstractAction.SMALL_ICON, IconFactory.getImage("find.png"));
		putValue(AbstractAction.NAME, _("Find..."));
		putValue(AbstractAction.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainGUI.addFindPanel();
	}
}
