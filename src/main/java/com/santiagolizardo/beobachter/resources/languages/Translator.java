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
package com.santiagolizardo.beobachter.resources.languages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import com.santiagolizardo.beobachter.util.LocaleUtil;

/**
 * This class is the responsible of translate every literal on the GUI
 * 
 * @author slizardo
 * 
 */
public class Translator {

	private static final Logger logger = Logger.getLogger(Translator.class
			.getName());

	private static I18n i18n;

	public static void start(String language) {
		Locale locale = LocaleUtil.fromString(language);

		try {
			i18n = I18nFactory.getI18n(Translator.class, locale);
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
		}
	}

	public static String _(String key) {
		if (null == i18n)
			return key;

		try {
			return i18n.tr(key);
		} catch (MissingResourceException mre) {
			logger.warning("Missing translation: " + key);
			return key;
		}
	}
}
