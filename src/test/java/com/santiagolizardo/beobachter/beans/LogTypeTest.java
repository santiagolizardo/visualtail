package com.santiagolizardo.beobachter.beans;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.Rule;

public class LogTypeTest {

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidName() {
		new LogType(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRefreshInterval() {
		new LogType("foobar", 50);
	}

	@Test
	public void testBasicProperties() {
		String name = "fooBar";
		LogType logType = new LogType(name);
		assertEquals(name, logType.getName());
		assertEquals(LogType.DEFAULT_REFRESH_INTERVAL_MS,
				logType.getRefreshInterval());
	}

	@Test
	public void testRules() {
		LogType logType = new LogType("fooBar");
		assertEquals(0, logType.getRules().size());
		logType.addRule(new Rule());
		assertEquals(1, logType.getRules().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRule() {
		LogType logType = new LogType("fooBar");
		logType.addRule(null);
	}
}
