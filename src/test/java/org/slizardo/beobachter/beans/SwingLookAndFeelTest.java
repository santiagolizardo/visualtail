package org.slizardo.beobachter.beans;

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
