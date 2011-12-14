package com.jeroensteenbeeke.bk.jayconomy.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {
	@Test
	public void testNumber() {
		Pattern p = SignBlockListener.PATTERN_AMOUNT;

		assertTrue(p.matcher("25").matches());
		assertTrue(p.matcher("1").matches());
		assertFalse(p.matcher("").matches());
		assertTrue(p.matcher("255").matches());
		assertFalse(p.matcher(" 25").matches());
		assertFalse(p.matcher(" 25 ").matches());
		assertFalse(p.matcher("25 ").matches());

	}

	@Test
	public void testItemCode() {
		Pattern p = SignBlockListener.PATTERN_ITEMCODE;

		assertTrue(p.matcher("25").matches());
		assertTrue(p.matcher("1").matches());
		assertFalse(p.matcher("").matches());
		assertTrue(p.matcher("255").matches());
		assertFalse(p.matcher(" 25").matches());
		assertFalse(p.matcher(" 25 ").matches());
		assertFalse(p.matcher("25 ").matches());
		assertTrue(p.matcher("25-5").matches());
		assertFalse(p.matcher("25-").matches());

		assertMatch(p, "25", 1, "25");
		assertMatch(p, "1", 1, "1");
		assertMatch(p, "25-2", 1, "25");
		assertMatch(p, "25-2", 3, "2");
	}

	@Test
	public void testCash() {
		Pattern p = SignBlockListener.PATTERN_DOLLAR;

		assertFalse(p.matcher("25").matches());
		assertFalse(p.matcher("1").matches());
		assertFalse(p.matcher("").matches());
		assertFalse(p.matcher("255").matches());
		assertFalse(p.matcher(" 25").matches());
		assertFalse(p.matcher(" 25 ").matches());
		assertFalse(p.matcher("25 ").matches());

		assertMatch(p, "$25", 1, "25");
		assertMatch(p, "$2", 1, "2");
		assertMatch(p, "$2.5", 1, "2.5");
		assertMatch(p, "$ 2", 1, "2");
		assertMatch(p, "$ 2.5", 1, "2.5");
	}

	@Test
	public void testAmountPlusMax() {
		Pattern p = SignBlockListener.PATTERN_AMOUNT_PLUS_MAX;

		assertFalse(p.matcher("25").matches());
		assertFalse(p.matcher("1").matches());
		assertFalse(p.matcher("").matches());
		assertFalse(p.matcher("255").matches());
		assertFalse(p.matcher(" 25").matches());
		assertFalse(p.matcher(" 25 ").matches());
		assertFalse(p.matcher("25 ").matches());

		assertFalse(p.matcher("25 max ").matches());
		assertFalse(p.matcher(" max ").matches());
		assertTrue(p.matcher("0 max 0").matches());

		assertMatch(p, "25 max 5", 1, "25");
		assertMatch(p, "25 max 5", 2, "5");
		assertMatch(p, "1 max 1", 1, "1");
		assertMatch(p, "1 max 1", 2, "1");
	}

	private void assertMatch(Pattern pattern, String input, int group,
			String expected) {
		Matcher m = pattern.matcher(input);
		assertTrue(m.matches());
		assertEquals(expected, m.group(group));
	}
}
