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
package com.santiagolizardo.visualtail.engine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tail {

	private static final Logger logger = Logger.getLogger(Tail.class.getName());

	private String lineSeparator;
	private int lineSeparatorLength;

	private File file;
	private long currentPosition;
	private long currentPositionBackwards;

	public Tail(String fileName) {
		setLineSeparator(System.lineSeparator());

		file = new File(fileName);
		open();
	}

	public void setLineSeparator(String lineSeparatorArg) {
		lineSeparator = lineSeparatorArg;
		lineSeparatorLength = lineSeparatorArg.length();
	}

	public void open() {
		open(file.length());
	}

	public void open(long position) {
		if (position < 0) {
			throw new IllegalArgumentException("Argument position must be positive");
		}

		currentPositionBackwards
				= currentPosition = (position > file.length() ? file.length() : position);
	}

	public boolean hasMoreLines() {
		return (file.length() > currentPosition);
	}

	public String readNextLine() {
		return readNextLines(1).get(0);
	}

	public List<String> readNextLines(int count) {
		if (count < 1) {
			throw new IllegalArgumentException("Argument count must greater than 0");
		}

		List<String> lines = new ArrayList<>();

		if (hasMoreLines()) {
			long fileLen = file.length();
			try (RandomAccessFile accessFile = new RandomAccessFile(
					file, "r")) {
				accessFile.seek(currentPosition);

				while (0 != count-- && currentPosition < fileLen) {
					String line = accessFile.readLine();
					currentPosition = accessFile.getFilePointer();
					lines.add(line);
				}

			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}

		return lines;
	}

	public String readPreviousLine() {
		List<String> lines = readPreviousLines(1);
		return lines.size() > 0 ? lines.get(0) : null;
	}

	public List<String> readPreviousLines(int count) {
		if (count < 1) {
			throw new IllegalArgumentException("Argument count must greater than 0");
		}

		List<String> lines = new ArrayList<>();

		long tempPosition = currentPositionBackwards - lineSeparatorLength;
		if (tempPosition < 1) {
			return lines;
		}

		byte chars[] = new byte[lineSeparatorLength];

		try (RandomAccessFile accessFile = new RandomAccessFile(
				file, "r")) {

			do {
				tempPosition--;
				accessFile.seek(tempPosition);
				accessFile.read(chars);
				if (new String(chars).equals(lineSeparator)) {
					String line = accessFile.readLine();
					lines.add(line);
					count--;
				}

			} while (tempPosition > lineSeparatorLength && count > 0);

			if (count > 0 && tempPosition != 0) {
				accessFile.seek(0);
				lines.add(accessFile.readLine());
			}

			currentPositionBackwards = tempPosition;
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

		return lines;
	}

	public long getCurrentPosition() {
		return currentPosition;
	}
}
