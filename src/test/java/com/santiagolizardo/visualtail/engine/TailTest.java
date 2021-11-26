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
package com.santiagolizardo.visualtail.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TailTest {

	private static final String lineSeparator = System.getProperty("line.separator");

	private File file;

	@Before
	public void setUp() throws FileNotFoundException, IOException {
		file = new File("test.log");

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(("first line" + lineSeparator).getBytes());
			fos.write(("second line" + lineSeparator).getBytes());
			fos.write(("third line" + lineSeparator).getBytes());
			fos.write(("last line" + lineSeparator).getBytes());
		}
	}

	@After
	public void tearDown() {
		file.delete();
	}

	@Test
	public void testDefaultOpenSetPositionToFileLength() {
		Tail tail = new Tail(file.getName());
		tail.open();
		assertEquals(file.length(), tail.getCurrentPosition());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOpenThrowsExceptionOnWrongPosition() {
		Tail tail = new Tail(file.getName());
		tail.open(-1);
	}

	@Test
	public void testOpenDefaultsToFileLengthMaximum() {
		Tail tail = new Tail(file.getName());
		tail.open(file.length() + 1000);
		assertEquals(file.length(), tail.getCurrentPosition());
	}

	@Test
	public void testReadLineReturnsExpectedString() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + lineSeparator).length());
		assertEquals("second line", tail.readNextLine());
	}

	@Test
	public void testReadLinesReturnsExpectedStrings() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + lineSeparator).length());
		assertEquals("second line", tail.readNextLine());
		assertEquals("third line", tail.readNextLine());
		assertEquals("last line", tail.readNextLine());
	}

	@Test
	public void testReadPreviousLineOneByOne() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + lineSeparator + "second line" + lineSeparator).length());
		assertEquals("second line", tail.readPreviousLine());
		assertEquals("first line", tail.readPreviousLine());
	}

	@Test
	public void testReadPreviousLineReturnsExpectedString() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + lineSeparator + "second line" + lineSeparator).length());
		List<String> previousLines = tail.readPreviousLines(2);
		assertEquals(2, previousLines.size());
		assertEquals("second line", previousLines.get(0));
		assertEquals("first line", previousLines.get(1));
	}

	@Test
	public void testReadPreviousLinesReturnsExpectedStrings() {
		Tail tail = new Tail(file.getName());
		tail.open(
				("first line" + lineSeparator + "second line" + lineSeparator + "third line" + lineSeparator).length());
		List<String> previousLines = tail.readPreviousLines(2);
		assertEquals(2, previousLines.size());
		assertEquals("third line", previousLines.get(0));
		assertEquals("second line", previousLines.get(1));
	}

	@Test
	public void testReadPreviousLinesReturnsMaxExistingLines() {
		Tail tail = new Tail(file.getName());
		tail.open(
				("first line" + lineSeparator + "second line" + lineSeparator + "third line" + lineSeparator).length());
		List<String> previousLines = tail.readPreviousLines(10);
		assertEquals(3, previousLines.size());
		assertEquals("third line", previousLines.get(0));
		assertEquals("second line", previousLines.get(1));
		assertEquals("first line", previousLines.get(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadPreviousLineThrowsExceptionOnInvalidCount() {
		Tail tail = new Tail(file.getName());
		tail.open();
		tail.readPreviousLines(0);
	}
}
