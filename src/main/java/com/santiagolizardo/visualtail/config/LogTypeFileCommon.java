/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.config;

import static com.santiagolizardo.visualtail.Constants.HOME_PATH;
import java.io.File;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;

public abstract class LogTypeFileCommon {

	public static final String FOLDER_LOG_TYPES = HOME_PATH + File.separator
			+ "logTypes";
	
	protected static final String LOG_TYPE_FILE_EXTENSION = ".xml";
	
	protected static final String TAG_LOG_TYPE = "log-type";	
	protected static final String TAG_FOREGROUND_COLOR = "foreground-color";
	protected static final String TAG_BACKGROUND_COLOR = "background-color";
	protected static final String TAG_IGNORE_CASE = "ignore-case";
	protected static final String TAG_REGULAR_EXPRESSION = "regular-expression";
	protected static final String TAG_PATTERN = "pattern";
	protected static final String TAG_RULE = "rule";
	protected static final String TAG_REFRESH_INTERVAL = "refresh-interval";
	
	protected static final Logger logger = Logger.getLogger(LogTypeFileCommon.class.getName());	
	
	protected DocumentBuilderFactory documentBuilderFactory;	

	public LogTypeFileCommon() {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();

		File dirLogTypes = new File(FOLDER_LOG_TYPES);
		if (!dirLogTypes.exists()) {
			dirLogTypes.mkdirs();
		}
	}	
	
	public String getPath(String name) {
		return FOLDER_LOG_TYPES + File.separator + name
				+ LOG_TYPE_FILE_EXTENSION;
	}	
}
