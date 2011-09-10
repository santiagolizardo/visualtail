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
package org.slizardo.beobachter.gui.renderers;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.beans.Rule;
import org.slizardo.beobachter.config.ConfigManager;

public class LineRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3132401719842667709L;

	private List<Rule> rules;

	public LineRenderer() {
		ConfigManager configManager = MainGUI.instance.configManager;

		Font font = new Font(configManager.getFontFamily(), Font.PLAIN,
				configManager.getFontSize());

		setOpaque(true);
		setFont(font);
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {
		setText(value.toString());
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);

		if (isSelected) {
			setBackground(Color.BLUE);
			setForeground(Color.WHITE);
		} else {
			for(Rule rule : rules) {
				if (rule.match(getText())) {
					setBackground(rule.getBackgroundColor());
					setForeground(rule.getForegroundColor());
				}
			}
		}

		return this;
	}
}
