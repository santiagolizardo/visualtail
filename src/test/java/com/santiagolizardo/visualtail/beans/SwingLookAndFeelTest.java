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
package com.santiagolizardo.visualtail.beans;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class SwingLookAndFeelTest {

	@Test
	public void testEquals() {
		SwingLookAndFeel laf1 = null, laf2 = null;

		laf1 = new SwingLookAndFeel("foo", "test.Foo");
		laf2 = new SwingLookAndFeel("bar", "test.Foo");
		assertEquals(laf1, laf2);

		laf1 = new SwingLookAndFeel("foo", "test.Foo");
		laf2 = new SwingLookAndFeel("foo", "test.Bar");
		assertThat(laf1, not(equalTo(laf2)));
	}
}
