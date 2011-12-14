package com.jeroensteenbeeke.bk.basics.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class MapOp {

	private MapOp() {
	}

	public static <K, V> void put(Map<K, List<V>> map, K key, V value) {
		List<V> vals = null;
		if (map.containsKey(key)) {
			vals = map.get(key);
		} else {
			vals = new LinkedList<V>();
		}

		vals.add(value);
		map.put(key, vals);

	}

}
