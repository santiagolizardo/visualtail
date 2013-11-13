/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2013 Santiago Lizardo (http://www.santiagolizardo.com)
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
package com.santiagolizardo.beobachter.gui.renderers;


import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.santiagolizardo.beobachter.util.LocaleUtil;

public class LocaleRender extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6554618278841155838L;

	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
		return super.getListCellRendererComponent(list, LocaleUtil.getDisplayName(value), index, isSelected,
				cellHasFocus);
	}
}