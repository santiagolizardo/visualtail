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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Tail implements Runnable {

	private static final Logger logger = Logger.getLogger(Tail.class.getName());

	private List<TailListener> listeners;

	private String fileName;

	private File file;
	private long currentPosition;

	public Tail(String fileName) {
		listeners = new LinkedList<>();

		file = new File(fileName);
		currentPosition = file.length();

		this.fileName = fileName;
	}

	public void open() {
		open(file.length());
	}

	public void open(long position) {
		if (position < 0) {
			throw new IllegalArgumentException("Argument position must be positive");
		}

		currentPosition = (position > file.length() ? file.length() : position);
	}

	public String readNextLine() {
		return readNextLines(1).get(0);
	}

	public List<String> readNextLines(int count) {
		if(count < 1) {
			throw new IllegalArgumentException("Argument count must greater than 0");
		}
		
		List<String> lines = new ArrayList<>();

		long currentSize = file.length();
		if (currentSize > currentPosition) {
			try (RandomAccessFile accessFile = new RandomAccessFile(
					fileName, "r")) {
				accessFile.seek(currentPosition);

				while (0 != count--) {
					String line = accessFile.readLine();
					lines.add(line);
				}

				currentPosition = accessFile.getFilePointer();
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}

		return lines;
	}

	public String readPreviousLine() {
		return readPreviousLines(1).get(0);
	}

	public List<String> readPreviousLines(int count) {
		if(count < 1) {
			throw new IllegalArgumentException("Argument count must greater than 0");
		}
		
		List<String> lines = new ArrayList<>();

		String lineSeparator = System.lineSeparator();
		int lineSeparatorLen = lineSeparator.length();
		long tempPosition = currentPosition - lineSeparatorLen;

		byte chars[] = new byte[lineSeparatorLen];

		long lastFound = 0;

		try (RandomAccessFile accessFile = new RandomAccessFile(
				fileName, "r")) {

			do {
				tempPosition--;
				accessFile.seek(tempPosition);
				accessFile.read(chars);
				if (new String(chars).equals(lineSeparator)) {
					lastFound = tempPosition;
					String line = accessFile.readLine();
					lines.add(line);
					count--;
				}

			} while (tempPosition > lineSeparatorLen && count > 0);

			if (count > 0 && lastFound != 0) {
				accessFile.seek(0);
				lines.add(accessFile.readLine());
			}

			currentPosition = accessFile.getFilePointer();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

		return lines;
	}

	@Override
	public void run() {
	}

	public void addListener(TailListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners(String line) {
		for (TailListener listener : listeners) {
			listener.onFileChanges(line);
		}
	}

	public long getCurrentPosition() {
		return currentPosition;
	}
}
