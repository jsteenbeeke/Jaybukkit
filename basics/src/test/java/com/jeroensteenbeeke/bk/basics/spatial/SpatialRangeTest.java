package com.jeroensteenbeeke.bk.basics.spatial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SpatialRangeTest {
	@Test
	public void test1DRanges() {
		SpatialKey key = SpatialKeys.keyFor(531);

		SpatialRange1D range = (SpatialRange1D) key.getRange(10);

		assertTrue(range.contains(key));

		assertEquals(530, range.getMinX());
		assertEquals(540, range.getMaxX());

		range = (SpatialRange1D) key.getRange(100);

		assertTrue(range.contains(key));

		assertEquals(500, range.getMinX());
		assertEquals(600, range.getMaxX());

		range = (SpatialRange1D) key.getRange(1000);

		assertTrue(range.contains(key));

		assertEquals(0, range.getMinX());
		assertEquals(1000, range.getMaxX());

		range = (SpatialRange1D) key.getRange(10000);

		assertTrue(range.contains(key));

		assertEquals(0, range.getMinX());
		assertEquals(10000, range.getMaxX());
	}

	@Test
	public void test2DRanges() {
		SpatialKey key = SpatialKeys.keyFor(531, -232);

		SpatialRange2D range = (SpatialRange2D) key.getRange(10);

		assertEquals(530, range.getMinX());
		assertEquals(540, range.getMaxX());
		assertEquals(-240, range.getMinY());
		assertEquals(-230, range.getMaxY());

		assertTrue(range.contains(key));

		range = (SpatialRange2D) key.getRange(100);

		assertTrue(range.contains(key));

		assertEquals(500, range.getMinX());
		assertEquals(600, range.getMaxX());
		assertEquals(-300, range.getMinY());
		assertEquals(-200, range.getMaxY());

		range = (SpatialRange2D) key.getRange(1000);

		assertTrue(range.contains(key));

		assertEquals(0, range.getMinX());
		assertEquals(1000, range.getMaxX());
		assertEquals(-1000, range.getMinY());
		assertEquals(0, range.getMaxY());

		range = (SpatialRange2D) key.getRange(10000);

		assertTrue(range.contains(key));

		assertEquals(0, range.getMinX());
		assertEquals(10000, range.getMaxX());
		assertEquals(-10000, range.getMinY());
		assertEquals(0, range.getMaxY());
	}

	@Test
	public void test3DRanges() {
		SpatialKey key = SpatialKeys.keyFor(531, -232, 57878);

		SpatialRange3D range = (SpatialRange3D) key.getRange(10);

		assertEquals(530, range.getMinX());
		assertEquals(540, range.getMaxX());
		assertEquals(-240, range.getMinY());
		assertEquals(-230, range.getMaxY());
		assertEquals(57870, range.getMinZ());
		assertEquals(57880, range.getMaxZ());

		assertTrue(range.contains(key));

		range = (SpatialRange3D) key.getRange(100);

		assertTrue(range.contains(key));

		assertEquals(500, range.getMinX());
		assertEquals(600, range.getMaxX());
		assertEquals(-300, range.getMinY());
		assertEquals(-200, range.getMaxY());
		assertEquals(57800, range.getMinZ());
		assertEquals(57900, range.getMaxZ());

		range = (SpatialRange3D) key.getRange(1000);

		assertTrue(range.contains(key));

		assertEquals(0, range.getMinX());
		assertEquals(1000, range.getMaxX());
		assertEquals(-1000, range.getMinY());
		assertEquals(0, range.getMaxY());
		assertEquals(57000, range.getMinZ());
		assertEquals(58000, range.getMaxZ());

		range = (SpatialRange3D) key.getRange(10000);

		assertTrue(range.contains(key));

		assertEquals(0, range.getMinX());
		assertEquals(10000, range.getMaxX());
		assertEquals(-10000, range.getMinY());
		assertEquals(0, range.getMaxY());
		assertEquals(50000, range.getMinZ());
		assertEquals(60000, range.getMaxZ());
	}
}
