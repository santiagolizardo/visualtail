/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.components;

import java.awt.Graphics;
import javax.swing.JTextField;

/**
 * A JTextField with some enhancements (i.e. placeholder support)
 */
public class EnhancedTextField extends JTextField {
	
	private String placeholder;

	public EnhancedTextField(int columns) {
		super(columns);
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if( placeholder.isEmpty() || !getText().isEmpty() ) {
			return;
		}
		
		g.setColor(getDisabledTextColor());
		g.drawString(placeholder, getInsets().left + 2, getInsets().top + getFontMetrics(getFont()).getMaxAscent());
	}
}
