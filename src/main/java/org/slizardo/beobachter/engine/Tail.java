/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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
package org.slizardo.beobachter.engine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Tail extends Thread {

	private static final Logger logger = Logger.getLogger(Tail.class.getName());

	private boolean enabled;

	private List<TailListener> listeners;

	private File file;
	private RandomAccessFile accessFile;

	private long size;

	private String fileName;

	private int refreshInterval;

	public Tail(String fileName, int refreshInterval) {
		setName("Tail");
		listeners = new ArrayList<TailListener>();
		enabled = true;

		file = new File(fileName);
		size = file.length();
		this.fileName = fileName;
		this.refreshInterval = refreshInterval;
	}

	public void run() {
		while (enabled) {
			openFileIfNeeded();
			if (accessFile != null) {
				try {
					long newSize = accessFile.length();
					if (newSize > size) {
						accessFile.seek(size);
						String line = accessFile.readLine();
						do {
							notifyListeners(line);
						} while ((line = accessFile.readLine()) != null);
						size = newSize;
					}
					Thread.sleep(refreshInterval);
				} catch (IOException ioe) {
					logger.severe(ioe.getMessage());
				} catch (InterruptedException ie) {
					logger.severe(ie.getMessage());
				}
				try {
					accessFile.close();
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}
		}
	}

	private void openFileIfNeeded() {
		if (file.length() > size) {
			try {
				accessFile = new RandomAccessFile(fileName, "r");
				size = accessFile.length();
			} catch (Exception e) {
				logger.severe(e.getMessage());
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
		TailEvent tailEvent = new TailEvent(line);
		Iterator<TailListener> it = listeners.iterator();
		while (it.hasNext()) {
			TailListener listener = (TailListener) it.next();
			listener.onFileChanges(tailEvent);
		}
	}
}
