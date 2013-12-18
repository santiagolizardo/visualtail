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
package com.santiagolizardo.beobachter.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.santiagolizardo.beobachter.Constants;

public class LogType {

	public static final int DEFAULT_REFRESH_INTERVAL_MS = 300;
	public static final int MINIMUM_REFRESH_INTERVAL_MS = 100;

	private String name;
	private int refreshInterval;

	private List<Rule> rules;

	public LogType(String name) {
		this(name, DEFAULT_REFRESH_INTERVAL_MS);
	}

	public LogType(String name, int refreshInterval) {
		setName(name);
		setRefreshInterval(refreshInterval);

		rules = new ArrayList<Rule>();
	}

	public String getPath() {
		return Constants.FOLDER_LOG_TYPES + File.separator + name
				+ ".properties";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name can not be null");
		}
		this.name = name;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		if (refreshInterval < MINIMUM_REFRESH_INTERVAL_MS) {
			throw new IllegalArgumentException(
					"refreshInterval has to be greater than "
							+ MINIMUM_REFRESH_INTERVAL_MS + " ms");
		}
		this.refreshInterval = refreshInterval;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void addRule(Rule rule) {
		if (null == rule) {
			throw new IllegalArgumentException("rule can not be null");
		}
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
