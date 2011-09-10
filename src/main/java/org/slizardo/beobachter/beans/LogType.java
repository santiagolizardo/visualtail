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


import java.util.ArrayList;
import java.util.List;

import org.slizardo.beobachter.Constants;

public class LogType {

	private String name;
	private short refreshInterval;

	private List<Rule> rules;

	public LogType(String name) {
		setName(name);
		setRefreshInterval((short) 500);

		rules = new ArrayList<Rule>();
	}

	public String getPath() {
		return Constants.FOLDER_LOG_TYPES + Constants.DIR_SEP + name
				+ ".properties";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(short refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public boolean equals(Object o) {
		if (o instanceof LogType) {
			LogType logType = (LogType) o;
			return getName().equals(logType.getName());
		}

		return false;
	}

	public int hashCode() {
		return getName().hashCode();
	}

	public String toString() {
		return String.format("LogType [ name(%s), refreshInterval(%d) ]", name,
				refreshInterval);
	}
}
