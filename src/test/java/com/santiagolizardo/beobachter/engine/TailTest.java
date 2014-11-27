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
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TailTest {

	private File file;

	@Before
	public void setUp() {
		file = new File("Test.log");
	}

	@After
	public void tearDown() {
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
		tail.open(("first line" + System.lineSeparator()).length());
		assertEquals("second line", tail.readNextLine());
	}

	@Test
	public void testReadLinesReturnsExpectedStrings() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + System.lineSeparator()).length());
		assertEquals("second line", tail.readNextLine());
		assertEquals("third line", tail.readNextLine());
		assertEquals("last line", tail.readNextLine());
	}

	@Test
	public void testReadPreviousLineReturnsExpectedString4() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + System.lineSeparator() + "second line" + System.lineSeparator()).length());
		assertEquals("second line", tail.readPreviousLine());
		assertEquals("first line", tail.readPreviousLine());
	}
	
	@Test
	public void testReadPreviousLineReturnsExpectedString() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + System.lineSeparator() + "second line" + System.lineSeparator()).length());
		List<String> previousLines = tail.readPreviousLines(2);
		assertEquals(2, previousLines.size());
		assertEquals("second line", previousLines.get(0));
		assertEquals("first line", previousLines.get(1));
	}

	@Test
	public void testReadPreviousLineReturnsExpectedString2() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + System.lineSeparator() + "second line" + System.lineSeparator() + "third line" + System.lineSeparator()).length());
		List<String> previousLines = tail.readPreviousLines(2);
		assertEquals(2, previousLines.size());
		assertEquals("third line", previousLines.get(0));
		assertEquals("second line", previousLines.get(1));
	}

	@Test
	public void testReadPreviousLineReturnsExpectedString3() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + System.lineSeparator() + "second line" + System.lineSeparator() + "third line" + System.lineSeparator()).length());
		List<String> previousLines = tail.readPreviousLines(10);
		assertEquals(3, previousLines.size());
		assertEquals("third line", previousLines.get(0));
		assertEquals("second line", previousLines.get(1));
		assertEquals("first line", previousLines.get(2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReadPreviousLineReturnsExpectedString6() {
		Tail tail = new Tail(file.getName());
		tail.open(("first line" + System.lineSeparator() + "second line" + System.lineSeparator() + "third line" + System.lineSeparator()).length());
		tail.readPreviousLines(0);
	}
}
