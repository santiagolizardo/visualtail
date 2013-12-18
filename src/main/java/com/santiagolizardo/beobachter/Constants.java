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
package com.santiagolizardo.beobachter;

import java.io.File;

/**
 * This class holds all the immutable information of the application.
 */
public class Constants {

	public static final String APP_NAME = "Beobachter";
	public static final String APP_VERSION = "1.7.7";
	public static final String APP_URL = "http://sourceforge.net/projects/beobachter/";
	public static final String APP_UPDATE_URL = "http://beobachter.sourceforge.net/version.html";

	public static final int RECENTS_LIMIT = 5;

	public static final String LINE_SEP = System.getProperty("line.separator");

	public static final String USER_HOME = System.getProperty("user.home");

	public static final String HOME_PATH = USER_HOME + File.separator
			+ ".beobachter";
	public static final String DATA_PATH = HOME_PATH + File.separator + "data";
	public static final String FOLDER_LOG_TYPES = DATA_PATH + File.separator
			+ "logTypes";
	public static final String FOLDER_SESSIONS = DATA_PATH + File.separator
			+ "sessions";

	public static final String CONFIG_FILE = HOME_PATH + File.separator
			+ "config.properties";
}
