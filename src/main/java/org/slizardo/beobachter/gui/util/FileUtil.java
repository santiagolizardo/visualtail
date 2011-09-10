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
package org.slizardo.beobachter.gui.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slizardo.beobachter.resources.languages.Translator;

public class FileUtil {

	public static void isReadable(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception(Translator.t("The_file_doesnt_exists"));
		}
		if (file.isDirectory()) {
			throw new Exception(Translator.t("Cannot_open_a_directory"));
		}
		if (!file.canRead()) {
			throw new Exception(Translator
					.t("You_dont_have_permission_to_read_this_file"));
		}
	}

	public static boolean copy(InputStream is, File file) {
		try {
			IOUtils.copy(is, new FileOutputStream(file));

			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());

			return false;
		}
	}
}