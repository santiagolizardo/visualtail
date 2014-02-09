/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Beobachter is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Beobachter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.config;

import java.util.List;
import java.util.logging.Logger;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.Rule;
import java.io.IOException;

public class EntitiesConfiguration {

	private static final String REFRESH_INTERVAL = "option.refresh_interval";

	private static String buildPath(String name) {
		return String.format("%s/%s.properties", Constants.FOLDER_LOG_TYPES,
				name);
	}

	public static LogType loadFromFile(String name) {
		String path = buildPath(name);
		PropertySet configuration = new PropertySet(
				path);

		LogType logType = new LogType(name);
		logType.setRefreshInterval(configuration.getShort(REFRESH_INTERVAL));

		for (short i = 0; configuration.getProperty("rule." + i
				+ ".regular_expression") != null; i++) {
			Rule rule = new Rule();
			String pattern = configuration.getProperty("rule." + i
					+ ".pattern");
			rule.setPattern(null == pattern ? "" : pattern);
			rule.setRegularExpression(configuration.getBoolean("rule." + i
					+ ".regular_expression"));
			rule.setIgnoreCase(configuration.getBoolean("rule." + i
					+ ".ignore_case"));
			rule.setBackgroundColor(configuration.getColor("rule." + i
					+ ".background_color"));
			rule.setForegroundColor(configuration.getColor("rule." + i
					+ ".foreground_color"));

			logType.addRule(rule);
		}

		return logType;
	}

	public static void saveToFile(LogType logType) throws IOException {
		PropertySet configuration = new PropertySet();

		configuration.setProperty(REFRESH_INTERVAL,
				logType.getRefreshInterval());

		List<Rule> rules = logType.getRules();
		for (int i = 0; i < rules.size(); i++) {
			Rule rule = (Rule) rules.get(i);
			configuration.setProperty("rule." + i + ".pattern",
					rule.getPattern());
			configuration.setProperty("rule." + i + ".regular_expression",
					rule.isRegularExpression());
			configuration.setProperty("rule." + i + ".ignore_case",
					rule.isIgnoreCase());
			configuration.setColor("rule." + i + ".background_color",
					rule.getBackgroundColor());
			configuration.setColor("rule." + i + ".foreground_color",
					rule.getForegroundColor());
		}

		String path = buildPath(logType.getName());
		configuration.save(path);
	}
}
