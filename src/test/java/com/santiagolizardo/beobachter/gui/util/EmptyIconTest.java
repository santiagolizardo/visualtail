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
