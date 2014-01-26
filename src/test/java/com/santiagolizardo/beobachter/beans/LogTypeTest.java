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
