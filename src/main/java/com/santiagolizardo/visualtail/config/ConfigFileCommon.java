/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.config;

import static com.santiagolizardo.visualtail.Constants.HOME_PATH;
import java.io.File;
import java.util.logging.Logger;

public abstract class ConfigFileCommon {

	protected static final String CONFIG_FILE_PATH = HOME_PATH + File.separator
			+ "config.xml";
	
	protected static final String TAG_CONFIG_DATA = "config-data";
	protected static final String TAG_PATH = "path";
	protected static final String TAG_NAME = "name";
	protected static final String TAG_SESSION = "session";
	protected static final String TAG_SESSIONS = "sessions";
	protected static final String TAG_RECENT_FILES = "recent-files";
	protected static final String TAG_SIZE = "size";
	protected static final String TAG_FAMILY = "family";
	protected static final String TAG_FONT = "font";
	protected static final String TAG_H = "height";
	protected static final String TAG_W = "width";
	protected static final String TAG_Y = "y";
	protected static final String TAG_X = "x";
	protected static final String TAG_LOOK_AND_FEEL = "look-and-feel";
	protected static final String TAG_WINDOW = "window";
	protected static final String TAG_LANGUAGE = "language";	
	
	protected static final Logger logger = Logger.getLogger(ConfigFileCommon.class.getName());
}
