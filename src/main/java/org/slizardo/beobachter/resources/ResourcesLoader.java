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
package org.slizardo.beobachter.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourcesLoader {

	public static String readResource(Class<?> clazz, String name) {
		StringBuffer buffer = new StringBuffer();

		try {
			InputStream is = clazz.getResourceAsStream(name);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static Collection<String> getResources(Pattern pattern)
			throws IOException {
		List<String> names = new ArrayList<String>();

		final String classPath = System.getProperty("java.class.path");
		String path = classPath.split(File.pathSeparator)[0];
		File file = new File(path);
		if (file.isDirectory()) {
			addResourcesFromDirectory(names, file, pattern);
		} else {
			addResourcesFromJar(names, file, pattern);
		}

		return names;
	}

	private static void addResourcesFromJar(List<String> names, File file,
			Pattern pattern) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<?> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String fileName = entry.getName();
			Matcher matcher = pattern.matcher(fileName);
			if (matcher.matches()) {
				String result = matcher.group(1);
				if (!names.contains(result)) {
					names.add(result);
				}
			}
		}
		zipFile.close();
	}

	private static void addResourcesFromDirectory(List<String> names,
			File directory, Pattern pattern) throws IOException {
		File[] fileList = directory.listFiles();
		for (File file : fileList) {
			if (file.isDirectory()) {
				addResourcesFromDirectory(names, file, pattern);
			} else {
				String fileName = file.getCanonicalPath();
				Matcher matcher = pattern.matcher(fileName);
				if (matcher.matches()) {
					String result = matcher.group(1);
					if (!names.contains(result)) {
						names.add(result);
					}
				}
			}
		}
	}
}
