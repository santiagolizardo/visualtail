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
package com.santiagolizardo.beobachter.gui.util;

import java.io.File;

import com.santiagolizardo.beobachter.resources.languages.Translator;

public class FileUtil {

	public static void tryReading(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception(Translator._("The_file_doesnt_exists"));
		}
		if (file.isDirectory()) {
			throw new Exception(Translator._("Cannot_open_a_directory"));
		}
		if (!file.canRead()) {
			throw new Exception(
					Translator._("You_dont_have_permission_to_read_this_file"));
		}
	}
}
