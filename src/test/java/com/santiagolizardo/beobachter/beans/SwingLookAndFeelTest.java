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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.santiagolizardo.beobachter.beans.SwingLookAndFeel;

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
