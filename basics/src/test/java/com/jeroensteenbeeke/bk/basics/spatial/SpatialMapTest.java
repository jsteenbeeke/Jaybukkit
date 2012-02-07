package com.jeroensteenbeeke.bk.basics.spatial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class SpatialMapTest {
	@Test
	public void insertContainsTest() {
		Map<SpatialKey, String> map = new SpatialMap<SpatialKey, String>(2, 10);

		String foo = "foo";
		SpatialKey key = SpatialKeys.keyFor(2, 2);

		map.put(key, foo);

		assertEquals(1, map.size());

		assertTrue(map.containsKey(key));
		assertTrue(map.containsValue(foo));

		assertEquals(foo, map.get(key));

		SpatialKey key2 = SpatialKeys.keyFor(-2, -2);
		String bar = "bar";

		map.put(key2, bar);

		assertEquals(2, map.size());

		assertTrue(map.containsKey(key2));
		assertTrue(map.containsValue(bar));
		assertTrue(map.containsKey(key));
		assertTrue(map.containsValue(foo));

		assertEquals(foo, map.get(key));
		assertEquals(bar, map.get(key2));
	}

	@Test
	public void insertRemoveTest() {
		Map<SpatialKey, String> map = new SpatialMap<SpatialKey, String>(2, 10);

		String foo = "foo";
		String bar = "bar";
		SpatialKey key = SpatialKeys.keyFor(2, 2);
		SpatialKey key2 = SpatialKeys.keyFor(-2, -2);

		map.put(key, foo);
		map.put(key2, bar);

		assertTrue(map.containsKey(key2));
		assertTrue(map.containsValue(bar));
		assertTrue(map.containsKey(key));
		assertTrue(map.containsValue(foo));

		assertEquals(foo, map.get(key));
		assertEquals(bar, map.get(key2));

		assertEquals(2, map.size());

		String rfoo = map.remove(key);

		assertEquals(foo, rfoo);
		assertEquals(1, map.size());

		assertEquals(bar, map.get(key2));
		assertTrue(map.containsKey(key2));
		assertTrue(map.containsValue(bar));
		assertFalse(map.containsKey(key));
		assertFalse(map.containsValue(foo));

	}
}
