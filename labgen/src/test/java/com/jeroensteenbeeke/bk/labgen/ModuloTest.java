package com.jeroensteenbeeke.bk.labgen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeroensteenbeeke.bk.labgen.LabGenerator.Mode;

public class ModuloTest {
	@Test
	public void testNegatives() {
		assertEquals(3, Mode.formula(7));
		assertEquals(2, Mode.formula(6));
		assertEquals(1, Mode.formula(5));
		assertEquals(0, Mode.formula(4));
		assertEquals(3, Mode.formula(3));
		assertEquals(2, Mode.formula(2));
		assertEquals(1, Mode.formula(1));
		assertEquals(0, Mode.formula(0));
		assertEquals(3, Mode.formula(-1));
		assertEquals(2, Mode.formula(-2));
		assertEquals(1, Mode.formula(-3));
		assertEquals(0, Mode.formula(-4));

		assertEquals(Mode.TOP_RIGHT, Mode.getMode(-1, -1));
		assertEquals(Mode.RIGHT, Mode.getMode(-1, -2));
	}

}
