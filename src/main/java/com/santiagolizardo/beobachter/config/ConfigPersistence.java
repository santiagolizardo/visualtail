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
package com.santiagolizardo.beobachter.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;

import com.santiagolizardo.beobachter.gui.MainWindow;
import java.util.List;

public class ConfigPersistence {

	public PropertiesConfiguration loadProperties(String fileName)
			throws ConfigurationException, FileNotFoundException, IOException {
		File file = new File(fileName);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!file.exists()) {
			InputStream is = ConfigData.class
					.getResourceAsStream("default_config.properties");
			IOUtils.copy(is, new FileOutputStream(file));
		}

		return new PropertiesConfiguration(fileName);
	}

	public void saveProperties(MainWindow mainGUI,
			PropertiesConfiguration configuration)
			throws ConfigurationException {
		byte i = 0;

		List<String> recentFiles = mainGUI.getRecentFiles();
		for (; i < recentFiles.size(); i++) {
			String propertyName = "recent." + i + ".file_name";
			configuration.setProperty(propertyName, recentFiles.get(i)
					.toString());
		}
		for (; i < 10; i++) {
			String propertyName = "recent." + i + ".file_name";
			configuration.clearProperty(propertyName);
		}

		configuration.save();
	}
}
