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

	private boolean enabled;

	private List<TailListener> listeners;

	private String fileName;

	private int refreshInterval;

	private File file;
	private long currentSize;

	public Tail(String fileName, int refreshInterval) {

		listeners = new LinkedList<>();
		enabled = true;

		file = new File(fileName);
		currentSize = file.length();

		this.fileName = fileName;
		this.refreshInterval = refreshInterval;
	}

	public void run() {
		while (enabled) {
			if (file.length() > currentSize) {

				try {
					RandomAccessFile accessFile = new RandomAccessFile(
							fileName, "r");
					accessFile.seek(currentSize);

					String line = accessFile.readLine();
					do {
						if (!line.isEmpty()) {
							notifyListeners(line);
						}
					} while ((line = accessFile.readLine()) != null);

					currentSize = file.length();

					accessFile.close();
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}

			try {
				Thread.sleep(refreshInterval);
			} catch (InterruptedException e) {
				logger.warning(e.getMessage());
			}
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
