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
package com.santiagolizardo.visualtail.util;

import java.util.Locale;
import junit.framework.TestCase;

public class LocaleUtilTest extends TestCase {

	public void testgetDisplayName() {
		try {
			LocaleUtil.getDisplayName(null);
			fail("An IllegalArgumentException was expected.");
		} catch (IllegalArgumentException iae) {
			assertEquals("Argument can not be null.", iae.getMessage());
		}

		Locale.setDefault(Locale.ENGLISH);
		assertEquals("", LocaleUtil.getDisplayName(""));
		assertEquals("foobar", LocaleUtil.getDisplayName("foobar"));
		assertEquals("Spanish", LocaleUtil.getDisplayName("es"));
		assertEquals("Spanish (Argentina)", LocaleUtil.getDisplayName("es_AR"));
	}
}
