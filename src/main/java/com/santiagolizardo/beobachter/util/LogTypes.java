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
package com.santiagolizardo.beobachter.util;

import java.io.File;
import java.io.FileFilter;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.config.EntitiesConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LogTypes {

	private static final Logger logger = Logger.getLogger(LogTypes.class.getName());

	private static LogTypes singleton;

	public static final LogTypes getInstance() {
		if (null == singleton) {
			singleton = new LogTypes();
		}
		return singleton;
	}

	private List<LogType> logTypes;

	private LogTypes() {
		logTypes = new ArrayList<>();
	}

	public List<LogType> getAll() {
		logTypes.clear();
		logTypes.add(new LogType("Default"));

		File logTypesDir = new File(Constants.FOLDER_LOG_TYPES);
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
			LogType logType = EntitiesConfiguration.loadFromFile(name);
			logTypes.add(logType);
		}

		return logTypes;
	}
}
