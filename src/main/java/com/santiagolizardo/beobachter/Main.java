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
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.LogManager;

import javax.swing.SwingUtilities;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.config.ConfigPersistence;
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
			I18n i18n = I18nFactory.getI18n(Main.class, Locale.ENGLISH);
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
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
		} catch (Exception ex) {
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

				MainGUI.instance = new MainGUI(configData);
				MainGUI.instance.setVisible(true);
			}
		});
	}
}
