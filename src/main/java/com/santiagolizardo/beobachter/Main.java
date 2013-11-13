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
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

import com.santiagolizardo.beobachter.config.ConfigManager;
import com.santiagolizardo.beobachter.gui.util.SwingUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;

/**
 * This is the main application entry point. It initializes the main window.
 * 
 * @author slizardo
 */
public class Main {

	public static void main(String[] args) {

		try {
			Properties prop = System.getProperties();
			prop.setProperty("java.util.logging.config.file",
					"logging.properties");
			LogManager.getLogManager().readConfiguration();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		ConfigManager configManager = null;
		try {
			configManager = new ConfigManager(Constants.CONFIG_FILE);
			configManager.loadConfiguration();

			SwingUtil.setLookAndFeel(configManager.getWindowLAF());

			Translator.start(configManager);

			File dirLogTypes = new File(Constants.FOLDER_LOG_TYPES);
			if (!dirLogTypes.exists()) {
				dirLogTypes.mkdirs();
			}
			File dirSessions = new File(Constants.FOLDER_SESSIONS);
			if (!dirSessions.exists()) {
				dirSessions.mkdirs();
			}

			MainGUI instance = new MainGUI(configManager);
			instance.setVisible(true);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
}
