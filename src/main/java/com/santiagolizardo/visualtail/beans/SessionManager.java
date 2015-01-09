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
package com.santiagolizardo.visualtail.beans;

import static com.santiagolizardo.visualtail.Constants.HOME_PATH;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SessionManager {

	private static final Logger logger = Logger.getLogger(SessionManager.class.getName());

	public static final String SESSIONS_FOLDER = HOME_PATH + File.separator
			+ "sessions";

	public SessionManager() {
		File dirSessions = new File(SESSIONS_FOLDER);
		if (!dirSessions.exists()) {
			logger.info("Sessions folder does not exist. Creating a new one now.");
			dirSessions.mkdirs();
		}
	}

	public List<Session> getSessions() {
		List<Session> sessions = new ArrayList<>();

		File folder = new File(SESSIONS_FOLDER);
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".txt"));
			}
		};
		File[] files = folder.listFiles(filter);
		for (File file : files) {
			String name = file.getName().replaceAll("\\.txt", "");

			Session session = new Session();
			session.setName(name);

			try {
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = reader.readLine()) != null) {
						session.getFileNames().add(line);
					}
				}
			} catch (IOException ee) {
				logger.warning(ee.getMessage());
			}

			sessions.add(session);
		}

		return sessions;
	}

	public boolean delete(String name) {
		File file = createFileFromName(name);
		return file.delete();
	}

	public void save(String name, List<String> filePaths) throws IOException {
		File file = createFileFromName(name);
		try (FileWriter writer = new FileWriter(file, false)) {
			for (String filePath : filePaths) {
				writer.write(filePath + "\n");
			}
		}
	}

	private File createFileFromName(String name) {
		return new File(SESSIONS_FOLDER
				+ File.separator + name + ".txt");
	}
}
