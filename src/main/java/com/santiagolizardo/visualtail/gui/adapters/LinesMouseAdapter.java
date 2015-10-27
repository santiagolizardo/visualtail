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
package com.santiagolizardo.visualtail.gui.adapters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.menu.ContextualMenu;

public class LinesMouseAdapter extends MouseAdapter {

	private final ContextualMenu popupMenu;

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
