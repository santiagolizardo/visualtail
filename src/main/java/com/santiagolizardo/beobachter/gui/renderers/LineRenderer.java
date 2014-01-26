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
package com.santiagolizardo.beobachter.gui.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.santiagolizardo.beobachter.beans.Rule;
import com.santiagolizardo.beobachter.beans.RuleMatcher;
import com.santiagolizardo.beobachter.config.ConfigData;

public class LineRenderer extends JLabel implements ListCellRenderer<String> {

	private static final long serialVersionUID = 3132401719842667709L;

	private RuleMatcher[] ruleMatchers;

	public LineRenderer(List<Rule> rules, ConfigData configManager) {
		Font font = new Font(configManager.getFontFamily(), Font.PLAIN,
				configManager.getFontSize());

		setOpaque(true);
		setFont(font);

		ruleMatchers = new RuleMatcher[rules.size()];
		for (int i = 0; i < rules.size(); i++) {
			ruleMatchers[i] = new RuleMatcher(rules.get(i));
		}
	}

	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean hasFocus) {
		setText(value);
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);

		if (isSelected) {
			setBackground(Color.BLUE);
			setForeground(Color.WHITE);
		} else {
			for (RuleMatcher ruleMatcher : ruleMatchers) {
				if (ruleMatcher.matches(getText())) {
					Rule rule = ruleMatcher.getRule();
					setBackground(rule.getBackgroundColor());
					setForeground(rule.getForegroundColor());
				}
			}
		}

		return this;
	}
}
