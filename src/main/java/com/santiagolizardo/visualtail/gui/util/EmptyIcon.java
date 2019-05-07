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
package com.santiagolizardo.visualtail.gui.util;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class EmptyIcon implements Icon {

	public final static EmptyIcon SIZE_16 = new EmptyIcon(16);

	private final int size;

	public EmptyIcon(int size) {
		this.size = size;
	}

	@Override
	public int getIconHeight() {
		return size;
	}

	@Override
	public int getIconWidth() {
		return size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
	}
}
