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

import java.io.File;

import com.santiagolizardo.visualtail.resources.languages.Translator;

public class FileUtil {

	public static String byteCountToDisplaySize(long fileSizeInBytes) {
		String sizeToDisplay = String.valueOf(fileSizeInBytes);

		if (fileSizeInBytes < 1024) { // Bytes
			sizeToDisplay = String.format("%d B", fileSizeInBytes);
		} else if ((fileSizeInBytes / 1024) < 1024) { // Kbytes
			sizeToDisplay = String.format("%.2f KB", (float)(fileSizeInBytes / 1024));
		} else if ((fileSizeInBytes / 1024 / 1024) < 1024) { // Mbytes
			sizeToDisplay = String.format("%.2f MB", (float)(fileSizeInBytes / 1024 / 1024));
		} else if ((fileSizeInBytes / 1024 / 1024 / 1024) < 1024) { // Gbytes
			sizeToDisplay = String.format("%.2f GB", (float)(fileSizeInBytes / 1024 / 1024 / 1024));
		}

		return sizeToDisplay;
	}

	public static void tryReading(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception(Translator.tr("The file does not exist"));
		}
		if (file.isDirectory()) {
			throw new Exception(Translator.tr("Cannot open a directory"));
		}
		if (!file.canRead()) {
			throw new Exception(
					Translator.tr("You do not have permissions to read this file"));
		}
	}
}
