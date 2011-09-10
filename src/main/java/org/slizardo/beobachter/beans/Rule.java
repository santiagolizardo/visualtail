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
package org.slizardo.beobachter.beans;

import java.awt.Color;
import java.util.regex.Pattern;

public class Rule {

	public final String PATTERN = "pattern";
	public final String REGULAR_EXPRESSION = "regular_expression";
	public final String IGNORE_CASE = "ignore_case";
	public final String BACKGROUND_COLOR = "background_color";
	public final String FOREGROUND_COLOR = "foreground_color";
	
	private String pattern;
	
	private boolean regularExpression;

	private boolean ignoreCase;

	private Color backgroundColor;

	private Color foregroundColor;

	public Rule() {
		backgroundColor = Color.WHITE;
		foregroundColor = Color.BLACK;
		regularExpression = false;
		ignoreCase = false;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public boolean isRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(boolean regularExpression) {
		this.regularExpression = regularExpression;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public boolean match(String text) {
		if (regularExpression) {
			Pattern p = Pattern.compile(getPattern());
			return p.matcher(text).matches();
		} else {
			return (text.indexOf(getPattern()) != -1);
		}
	}
}
