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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RuleMatcherTest {

	@Test
	public void testPatternMatching() {
		Rule rule = new Rule();
		rule.setRegularExpression(true);
		rule.setPattern("[a-z]{1,3}");

		RuleMatcher ruleMatcher = new RuleMatcher(rule);

		assertTrue(ruleMatcher.matches("abc"));
		assertFalse(ruleMatcher.matches("123"));
		assertFalse(ruleMatcher.matches("abcd"));
		assertFalse(ruleMatcher.matches(null));
	}
}
