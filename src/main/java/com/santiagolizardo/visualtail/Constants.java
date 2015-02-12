/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail;

import java.io.File;

/**
 * This class holds all the immutable information of the application.
 */
public class Constants {

	public static final String APP_NAME = "VisualTail";
	public static final String APP_VERSION = "1.8.5";
	public static final String APP_URL = "https://github.com/santiagolizardo/visualtail";
	public static final String APP_UPDATE_URL = "http://visualtail.sourceforge.net/version.html";

	public static final String LINE_SEP = System.getProperty("line.separator");

	public static final String USER_HOME = System.getProperty("user.home");

	public static final String HOME_PATH = USER_HOME + File.separator
			+ ".visualtail";
}