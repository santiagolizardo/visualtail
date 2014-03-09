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
package com.santiagolizardo.beobachter.beans;

import java.io.File;
import java.io.FileFilter;

import static com.santiagolizardo.beobachter.Constants.HOME_PATH;
import com.santiagolizardo.beobachter.config.PropertySet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LogTypeManager {

	private static final Logger logger = Logger.getLogger(LogTypeManager.class.getName());

	public static final String FOLDER_LOG_TYPES = HOME_PATH + File.separator
			+ "logTypes";

	private static final String REFRESH_INTERVAL = "option.refresh_interval";

	private static LogTypeManager singleton;

	public static final LogTypeManager getInstance() {
		if (null == singleton) {
			singleton = new LogTypeManager();
		}
		return singleton;
	}

	private List<LogType> logTypes;

	private LogTypeManager() {
		logTypes = new ArrayList<>();

		File dirLogTypes = new File(FOLDER_LOG_TYPES);
		if (!dirLogTypes.exists()) {
			dirLogTypes.mkdirs();
		}
	}

	public List<LogType> getAll() {
		logTypes.clear();
		logTypes.add(new LogType("Default"));

		File logTypesDir = new File(FOLDER_LOG_TYPES);
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".properties"));
			}
		};
		File[] files = logTypesDir.listFiles(filter);
		for (File file : files) {
			String fileName = file.getName();
			String name = fileName.replaceAll(".properties", "");
			LogType logType = loadFromFile(name);
			logTypes.add(logType);
		}

		return logTypes;
	}

	public String getPath(String name) {
		return FOLDER_LOG_TYPES + File.separator + name
				+ ".properties";
	}

	public boolean remove(LogType logType) {
		String path = getPath(logType.getName());
		File file = new File(path);
		return file.delete();
	}

	public boolean rename(LogType logType, String newName) {
		String path = getPath(logType.getName());
		File file = new File(path);

		String newPath = getPath(newName);
		File newFile = new File(newPath);

		return file.renameTo(newFile);
	}

	public LogType loadFromFile(String name) {
		String path = getPath(name);
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

	public void saveToFile(LogType logType) throws IOException {
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

		String path = getPath(logType.getName());
		configuration.save(path);
	}
}
