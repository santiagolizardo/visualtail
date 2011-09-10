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
package org.slizardo.beobachter.gui.renderers;


import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.slizardo.beobachter.beans.LogType;

public class LogTypeListRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 6231566595982380949L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		String name = ((LogType) value).getName();
		return super.getListCellRendererComponent(list, name, index,
				isSelected, cellHasFocus);
	}
}