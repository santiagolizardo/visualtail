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
package com.santiagolizardo.beobachter.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.santiagolizardo.beobachter.resources.ResourcesLoader;

/**
 * Utilities for locale, regional settings.
 */
public class LocaleUtil {

	private static final Logger logger = Logger.getLogger(LocaleUtil.class
			.getName());

	/**
	 * Get the display name for an specific locale.
	 * 
	 * @param localeCode
	 * @return String
	 */
	public static String getDisplayName(String localeCode) {
		if (localeCode == null)
			throw new IllegalArgumentException("Argument can not be null.");
		Locale locale = fromString(localeCode);
		return locale.getDisplayName();
	}

	public static Locale fromString(String localeCode) {
		String[] parts = localeCode.split("_");
		Locale locale = parts.length == 1 ? new Locale(parts[0]) : new Locale(
				parts[0], parts[1]);
		return locale;
	}

	public static String[] getAvailableLocales() {
		Pattern pattern = Pattern.compile("^.*Translator_([a-zA-Z_]+).class$");

		Collection<String> files = null;
		try {
			files = ResourcesLoader.getResources(pattern);
			return files.toArray(new String[] {});
		} catch (IOException e) {
			logger.warning(e.getMessage());
			return new String[] {};
		}
	}
}
