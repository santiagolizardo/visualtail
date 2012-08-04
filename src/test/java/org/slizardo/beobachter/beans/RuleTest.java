package org.slizardo.beobachter.beans;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class RuleTest {

	@Test
	public void test() {
		Rule rule = new Rule();
		assertEquals(Color.WHITE, rule.getBackgroundColor());
		assertEquals(Color.BLACK, rule.getForegroundColor());
	}
}
