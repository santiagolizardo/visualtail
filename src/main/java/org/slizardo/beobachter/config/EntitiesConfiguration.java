/**
 * Beobachter, the universal logs watcher
 * Copyright (C) 2009  Santiago Lizardo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.slizardo.beobachter.config;


import java.awt.Color;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.beans.Rule;
import org.slizardo.beobachter.gui.util.SwingUtil;

public class EntitiesConfiguration {

	private static final String REFRESH_INTERVAL = "option.refresh_interval";

	private static String buildPath(String name) {
		return String.format("%s/%s.properties", Constants.FOLDER_LOG_TYPES,
				name);
	}

	public static LogType loadFromFile(String name)
			throws ConfigurationException {
		String path = buildPath(name);
		PropertiesConfiguration configuration = new PropertiesConfiguration(
				path);

		LogType logType = new LogType(name);
		logType.setRefreshInterval(configuration.getShort(REFRESH_INTERVAL));

		String pattern = null;
		for (short i = 0; (pattern = configuration.getString("rule." + i
				+ ".pattern")) != null; i++) {
			Rule rule = new Rule();
			rule.setPattern(pattern);
			rule.setRegularExpression(configuration.getBoolean("rule." + i
					+ ".regular_expression"));
			rule.setIgnoreCase(configuration.getBoolean("rule." + i
					+ ".ignore_case"));
			rule.setBackgroundColor(getColorProperty(configuration, "rule." + i
					+ ".background_color"));
			rule.setForegroundColor(getColorProperty(configuration, "rule." + i
					+ ".foreground_color"));

			logType.addRule(rule);
		}

		return logType;
	}

	public static void saveToFile(LogType logType)
			throws ConfigurationException {
		PropertiesConfiguration configuration = new PropertiesConfiguration();

		configuration.setProperty(REFRESH_INTERVAL, logType
				.getRefreshInterval());

		List<Rule> rules = logType.getRules();
		for (int i = 0; i < rules.size(); i++) {
			Rule rule = (Rule) rules.get(i);
			configuration.setProperty("rule." + i + ".pattern", rule
					.getPattern());
			configuration.setProperty("rule." + i + ".regular_expression", rule
					.isRegularExpression());
			configuration.setProperty("rule." + i + ".ignore_case", rule
					.isIgnoreCase());
			setColorProperty(configuration, "rule." + i + ".background_color",
					rule.getBackgroundColor());
			setColorProperty(configuration, "rule." + i + ".foreground_color",
					rule.getForegroundColor());
		}

		String path = buildPath(logType.getName());
		configuration.save(path);
	}

	public static void setColorProperty(PropertiesConfiguration configuration,
			String propertyName, Color propertyValue) {
		configuration.setProperty(propertyName, SwingUtil
				.colorToString(propertyValue));
	}

	public static Color getColorProperty(PropertiesConfiguration configuration,
			String propertyName) {
		return SwingUtil.stringToColor(configuration.getString(propertyName));
	}
}