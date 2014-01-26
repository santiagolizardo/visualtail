/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Beobachter is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Beobachter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.engine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Tail implements Runnable {

	private static final Logger logger = Logger.getLogger(Tail.class.getName());

	private List<TailListener> listeners;

	private String fileName;

	private File file;
	private long savedSize;

	public Tail(String fileName) {
		listeners = new LinkedList<>();

		file = new File(fileName);
		savedSize = file.length();

		this.fileName = fileName;
	}

	@Override
	public void run() {
		long currentSize = file.length();
		if (currentSize > savedSize) {

			try {
				RandomAccessFile accessFile = new RandomAccessFile(
						fileName, "r");
				accessFile.seek(savedSize);

				String line = accessFile.readLine();
				do {
					if (!line.isEmpty()) {
						notifyListeners(line);
					}
				} while ((line = accessFile.readLine()) != null);

				savedSize = currentSize;

				accessFile.close();
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	public void addListener(TailListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners(String line) {
		Iterator<TailListener> it = listeners.iterator();
		while (it.hasNext()) {
			TailListener listener = (TailListener) it.next();
			listener.onFileChanges(line);
		}
	}
}
