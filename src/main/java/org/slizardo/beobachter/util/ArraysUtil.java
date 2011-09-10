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
package org.slizardo.beobachter.util;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JMenu;

import org.apache.commons.configuration.ConfigurationException;
import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.config.EntitiesConfiguration;
import org.slizardo.beobachter.resources.ResourcesLoader;
import org.slizardo.beobachter.resources.languages.Translator;

public class ArraysUtil {

	public static Vector<String> recents = null;

	public static JMenu recentsMenu = null;

	public static Vector<LogType> logTypes = null;

	static {
		recents = new Vector<String>();
		logTypes = new Vector<LogType>();
	}

	public static String[] arrayLanguages() {
		ArrayList<String> result = new ArrayList<String>();

		Translator.class.getResource("languages");

		final Pattern pattern = Pattern
				.compile("^.*Translation_([^.]+)?.properties$");
		Collection<String> files;
		try {
			files = ResourcesLoader.getResources(pattern);
			for (String fileName : files) {
				result.add(fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (String[]) result.toArray(new String[] {});
	}

	public static Vector<String> arrayRecentFiles() {
		return recents;
	}

	public static Vector<LogType> arrayLogTypes() {
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
