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
}
