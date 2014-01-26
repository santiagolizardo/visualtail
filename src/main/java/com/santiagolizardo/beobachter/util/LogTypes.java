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
package com.santiagolizardo.beobachter.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import org.apache.commons.configuration.ConfigurationException;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.config.EntitiesConfiguration;

public class LogTypes {

	private static LogTypes singleton;

	public static final LogTypes getInstance() {
		if (null == singleton) {
			singleton = new LogTypes();
		}
		return singleton;
	}

	private Vector<LogType> logTypes;

	private LogTypes() {
		logTypes = new Vector<LogType>();
	}

	public Vector<LogType> getAll() {
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
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			try {
				String name = fileName.replaceAll(".properties", "");
				LogType logType = EntitiesConfiguration.loadFromFile(name);
				logTypes.add(logType);
			} catch (ConfigurationException ioe) {
				ioe.printStackTrace();
			}
		}

		return logTypes;
	}
}
