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
package com.santiagolizardo.beobachter.gui.adapters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.menu.ContextualMenu;

public class LinesMouseAdapter extends MouseAdapter {

	private ContextualMenu popupMenu;

	public LinesMouseAdapter(MainWindow mainWindow) {
		super();

		popupMenu = new ContextualMenu(mainWindow);
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		showPopup(ev);
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		showPopup(ev);
	}

	private void showPopup(MouseEvent ev) {
		if (ev.isPopupTrigger()) {
			popupMenu.show((JComponent) ev.getSource(), ev.getX(), ev.getY());
		}
	}
}
