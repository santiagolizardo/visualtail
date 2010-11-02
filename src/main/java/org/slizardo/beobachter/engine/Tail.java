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
package org.slizardo.beobachter.engine;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

public class Tail extends Thread {

	private boolean enabled;

	private ArrayList<TailListener> listeners;

	private RandomAccessFile file;

	private long size;

	private String fileName;

	private short refreshInterval;

	public Tail(String fileName, short refreshInterval) {

		setName("Tail");
		listeners = new ArrayList<TailListener>();
		enabled = true;
		this.fileName = fileName;
		this.refreshInterval = refreshInterval;
	}

	public void run() {
		while (file == null) {
			try {
				file = new RandomAccessFile(fileName, "r");
				size = file.length();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		while (enabled) {
			try {
				long newSize = file.length();
				if (newSize > size) {
					file.seek(size);
					String line = file.readLine();
					do {
						notifyListeners(line);
					} while ((line = file.readLine()) != null);
					size = newSize;
				}
				Thread.sleep(refreshInterval);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
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
