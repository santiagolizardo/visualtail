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

import static com.santiagolizardo.beobachter.Constants.HOME_PATH;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.santiagolizardo.beobachter.gui.MainWindow;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

public class ConfigPersistence {

	private static final Logger logger = Logger.getLogger(ConfigPersistence.class.getName());

	public static final String CONFIG_FILE_PATH = HOME_PATH + File.separator
			+ "config.properties";

	public PropertySet loadProperties()
			throws FileNotFoundException, IOException {
		File file = new File(CONFIG_FILE_PATH);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!file.exists()) {
			InputStream is = ConfigData.class
					.getResourceAsStream("default_config.properties");
			Files.copy(is, file.toPath());
		}

		return new PropertySet(CONFIG_FILE_PATH);
	}

	public void saveProperties(MainWindow mainWindow,
			PropertySet configuration) {
		byte i = 0;

		List<String> recentFiles = mainWindow.getRecentFiles();
		for (; i < recentFiles.size(); i++) {
			String propertyName = "recent." + i + ".file_name";
			configuration.setProperty(propertyName, recentFiles.get(i)
					.toString());
		}
		for (; i < 10; i++) {
			String propertyName = "recent." + i + ".file_name";
			configuration.remove(propertyName);
		}

		try {
			configuration.save();
		} catch (IOException ex) {
			logger.severe(ex.getMessage());
		}
	}
}
