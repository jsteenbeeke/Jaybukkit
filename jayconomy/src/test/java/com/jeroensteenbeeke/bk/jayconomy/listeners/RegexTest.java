/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
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
