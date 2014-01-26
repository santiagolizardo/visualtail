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
package com.santiagolizardo.beobachter.beans;

import java.util.regex.Pattern;

public class RuleMatcher {

	private Rule rule;
	private Pattern rePattern;

	public RuleMatcher(final Rule rule) {
		this.rule = rule;

		if (rule.isRegularExpression()) {
			rePattern = Pattern.compile(rule.getPattern());
		}
	}

	public Rule getRule() {
		return rule;
	}

	public boolean matches(final String text) {
		if (null == text) {
			return false;
		}

		String pattern = rule.getPattern();

		if (rule.isRegularExpression()) {
			return rePattern.matcher(text).matches();
		} else {
			return (text.indexOf(pattern) != -1);
		}
	}
}
