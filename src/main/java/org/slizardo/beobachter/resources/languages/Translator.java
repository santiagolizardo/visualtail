/**
 * Beobachter, the universal logs watcher
 * Copyright (C) 2009  Santiago Lizardo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.slizardo.beobachter.resources.languages;


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slizardo.beobachter.config.ConfigManager;

/**
 * This class is the responsible of translate every literal on the GUI
 * 
 * @author slizardo
 * 
 */
public class Translator {

	private static ResourceBundle bundle;

	public static void start(ConfigManager configManager) {
		String[] locales = configManager.getLanguage().split("_");
		Locale locale = new Locale(locales[0], locales[1]);
		bundle = ResourceBundle.getBundle("org.slizardo.beobachter.resources.languages.Translation", locale);
	}

	public static String t(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException mre) {
			return "!".concat(key);
		}
	}
}
