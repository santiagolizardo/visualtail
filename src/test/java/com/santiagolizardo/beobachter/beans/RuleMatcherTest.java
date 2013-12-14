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
