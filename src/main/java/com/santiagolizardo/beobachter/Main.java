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
package com.santiagolizardo.beobachter;

import com.santiagolizardo.beobachter.gui.MainWindow;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.LogManager;

import javax.swing.SwingUtilities;

import org.xnap.commons.i18n.I18nFactory;

import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.config.ConfigPersistence;
import com.santiagolizardo.beobachter.gui.util.SwingUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;

/**
 * This is the main application entry point. It initializes the main window.
 */
public class Main {

	public static void main(String[] args) {

		try {
			I18nFactory.getI18n(Main.class, Locale.ENGLISH);
		} catch (MissingResourceException mre) {
			System.err.println(mre.getMessage());
		}
		
		try {
			Properties prop = System.getProperties();
			prop.setProperty("java.util.logging.config.file",
					"logging.properties");
			LogManager.getLogManager().readConfiguration();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		final ConfigData configData = new ConfigData();
		ConfigPersistence configPersistence = new ConfigPersistence();
		try {
			configData.setConfiguration(configPersistence
					.loadProperties(Constants.CONFIG_FILE));
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		Translator.start(configData.getLanguage());

		File dirLogTypes = new File(Constants.FOLDER_LOG_TYPES);
		if (!dirLogTypes.exists()) {
			dirLogTypes.mkdirs();
		}
		File dirSessions = new File(Constants.FOLDER_SESSIONS);
		if (!dirSessions.exists()) {
			dirSessions.mkdirs();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				SwingUtil.setLookAndFeel(configData.getWindowLAF());

				MainWindow mainGUI = new MainWindow(configData);
				mainGUI.setVisible(true);
			}
		});
	}
}
