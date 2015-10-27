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

import com.santiagolizardo.visualtail.gui.MainWindow;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.LogManager;

import javax.swing.SwingUtilities;

import org.xnap.commons.i18n.I18nFactory;

import com.santiagolizardo.visualtail.config.ConfigData;
import com.santiagolizardo.visualtail.config.ConfigFileReader;
import com.santiagolizardo.visualtail.gui.util.SwingUtil;
import com.santiagolizardo.visualtail.resources.languages.Translator;

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

		ConfigFileReader configPersistence = new ConfigFileReader();
		final ConfigData configData = configPersistence.read();

		Translator.start(configData.getLanguage());

		SwingUtilities.invokeLater(() -> {
			SwingUtil.setLookAndFeel(configData.getWindowLookAndFeel());
			
			MainWindow mainWindow = new MainWindow(configData);
			mainWindow.setVisible(true);
		});
	}
}
