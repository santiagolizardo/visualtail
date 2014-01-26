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
package com.santiagolizardo.beobachter.gui.util;

import javax.swing.Icon;

import junit.framework.TestCase;

public class EmptyIconTest extends TestCase {

	public void testBothDimensionsAreEquals() {
		EmptyIcon emptyIcon = new EmptyIcon(10);
		assertEquals(emptyIcon.getIconWidth(), emptyIcon.getIconHeight());
	}

	public void testEmptyIcon16HasTheRigthDimension() {
		assertEquals(16, EmptyIcon.SIZE_16.getIconWidth());
		assertEquals(16, EmptyIcon.SIZE_16.getIconHeight());
	}

	public void testEmptyIconHasCorrectInheritance() {
		assertTrue(new EmptyIcon(0) instanceof Icon);
	}
}
