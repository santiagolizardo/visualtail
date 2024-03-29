/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later
  version.

  VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with
  VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.renderers;

import com.santiagolizardo.visualtail.beans.LogType;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.santiagolizardo.visualtail.beans.Rule;
import com.santiagolizardo.visualtail.beans.RuleMatcher;
import com.santiagolizardo.visualtail.config.ConfigData;
import java.util.regex.Pattern;

public class LineRenderer extends JLabel implements ListCellRenderer<String> {


	private RuleMatcher[] ruleMatchers;

	private Pattern replacePattern;
	private String replacementString;

	public LineRenderer() {
		setOpaque(true);
	}

	public void loadLogType(LogType logType) {
		List<Rule> rules = logType.getRules();
		ruleMatchers = new RuleMatcher[rules.size()];
		for (int i = 0; i < rules.size(); i++) {
			ruleMatchers[i] = new RuleMatcher(rules.get(i));
		}
	}

	public void updateFont(ConfigData configData) {
		setFont(configData.getFont());
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean hasFocus) {

		String lineText = (replacePattern != null ? replacePattern.matcher(value).replaceAll(replacementString) : value);

		setText(lineText);
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

	public void updateReplacerValues(Pattern replacePattern, String replacementString) {
		this.replacePattern = replacePattern;
		this.replacementString = replacementString;
	}
}
