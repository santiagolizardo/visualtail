package org.slizardo.beobachter.beans;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LogTypeTest {

	@Test
	public void testBasicProperties() {
		String name = "fooBar";
		LogType logType = new LogType(name);
		assertEquals(name, logType.getName());
	}

	@Test
	public void testRules() {
		LogType logType = new LogType("fooBar");
		assertEquals(0, logType.getRules().size());
		logType.addRule(null);
		assertEquals(1, logType.getRules().size());
	}
}
