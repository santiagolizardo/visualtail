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
package com.santiagolizardo.visualtail.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.santiagolizardo.visualtail.resources.ResourcesLoader;

/**
 * Utilities for locale, regional settings.
 */
public class LocaleUtil {

	private static final Logger logger = Logger.getLogger(LocaleUtil.class
			.getName());

	/**
	 * Get the display name for an specific locale.
	 * 
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
		return parts.length == 1 ? new Locale(parts[0]) : new Locale(
				parts[0], parts[1]);
	}

	public static String[] getAvailableLocales() {
		Pattern pattern = Pattern.compile("^.*Translator_([a-zA-Z_]+).class$");

		Collection<String> files;
		try {
			files = ResourcesLoader.getResources(pattern);
			return files.toArray(new String[] {});
		} catch (IOException e) {
			logger.warning(e.getMessage());
			return new String[] {};
		}
	}
}
