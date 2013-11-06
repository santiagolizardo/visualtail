package com.santiagolizardo.beobachter.beans;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import com.santiagolizardo.beobachter.beans.Rule;

public class RuleTest {

	@Test
	public void testDefaults() {
		Rule rule = new Rule();

		assertEquals(Color.WHITE, rule.getBackgroundColor());
		assertEquals(Color.BLACK, rule.getForegroundColor());
		assertFalse(rule.isRegularExpression());
		assertFalse(rule.isIgnoreCase());
	}

	@Test
	public void testPatternMatching() {
		Rule rule = new Rule();
		rule.setRegularExpression(true);
		rule.setPattern("[a-z]{1,3}");

		assertTrue(rule.match("abc"));
		assertFalse(rule.match("123"));
		assertFalse(rule.match("abcd"));
		assertFalse(rule.match(null));
	}
}
