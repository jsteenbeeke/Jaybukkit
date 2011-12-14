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
