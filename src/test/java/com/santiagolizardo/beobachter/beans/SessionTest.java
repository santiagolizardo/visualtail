package com.santiagolizardo.beobachter.beans;

import junit.framework.TestCase;

public class SessionTest extends TestCase {

	public void testInitialization() {
		Session session = new Session();
		assertNull(session.getName());
		assertTrue(session.getFileNames().isEmpty());
	}
}
