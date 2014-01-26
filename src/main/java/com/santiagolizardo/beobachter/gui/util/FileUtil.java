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
package com.santiagolizardo.beobachter.gui.util;

import java.io.File;

import com.santiagolizardo.beobachter.resources.languages.Translator;

public class FileUtil {

	public static void tryReading(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception(Translator._("The file does not exist"));
		}
		if (file.isDirectory()) {
			throw new Exception(Translator._("Cannot open a directory"));
		}
		if (!file.canRead()) {
			throw new Exception(
					Translator._("You do not have permissions to read this file"));
		}
	}
}

