package com.jeroensteenbeeke.bk.basics.spatial;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

public class SpatialMap<K extends SpatialKey, V> implements Map<K, V>,
		SpatialContainer<K, V> {
	private Map<SpatialRange, SpatialContainer<K, V>> containers;

	private final int levels;

	private final int stepSize;

	private final long factor;

	public SpatialMap(int levels, int stepSize) {
		containers = Maps.newHashMap();
		this.levels = levels >= 1 ? levels : 1;
		this.stepSize = stepSize;
		this.factor = (long) Math.pow(stepSize, levels);
	}

	@Override
	public void clear() {
		for (SpatialContainer<K, V> container : containers.values()) {
			container.clear();
		}

		containers.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		if (key instanceof SpatialKey) {
			SpatialKey k = (SpatialKey) key;
			SpatialRange range = k.getRange(factor);
			if (containers.containsKey(range)) {
				return containers.get(range).containsKey(k);
			}
		}

		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for (SpatialContainer<K, V> container : containers.values()) {
			if (container.containsValue(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> result = new HashSet<Map.Entry<K, V>>();
		for (SpatialContainer<K, V> container : containers.values()) {
			result.addAll(container.entrySet());
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((containers == null) ? 0 : containers.hashCode());
		result = prime * result + levels;
		result = prime * result + stepSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpatialMap<?, ?> other = (SpatialMap<?, ?>) obj;
		if (containers == null) {
			if (other.containers != null)
				return false;
		} else if (!containers.equals(other.containers))
			return false;
		if (levels != other.levels)
			return false;
		if (stepSize != other.stepSize)
			return false;
		return true;
	}

	@Override
	public V get(Object key) {
		if (key instanceof SpatialKey) {
			SpatialKey k = (SpatialKey) key;
			SpatialRange range = k.getRange(factor);
			if (containers.containsKey(range)) {
				return containers.get(range).get(key);
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		for (SpatialContainer<K, V> container : containers.values()) {
			if (!container.isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public Set<K> keySet() {
		Set<K> keys = new HashSet<K>();
		for (SpatialContainer<K, V> container : containers.values()) {
			keys.addAll(container.keySet());
		}
		return keys;
	}

	@Override
	public V remove(Object key) {
		if (key instanceof SpatialKey) {
			SpatialKey k = (SpatialKey) key;
			SpatialRange range = k.getRange(factor);
			if (containers.containsKey(range)) {
				return containers.get(range).remove(key);
			}
		}

		return null;
	}

	@Override
	public int size() {
		int size = 0;

		for (SpatialContainer<K, V> container : containers.values()) {
			size += container.size();
		}

		return size;
	}

	@Override
	public Collection<V> values() {
		Collection<V> retval = new HashSet<V>();
		for (SpatialContainer<K, V> container : containers.values()) {
			retval.addAll(container.values());
		}
		return retval;

	}

	public V put(K key, V value) {
		SpatialRange range = key.getRange(factor);

		if (!containers.containsKey(range)) {

			if (levels > 1) {
				containers.put(range,
						new SpatialMap<K, V>(levels - 1, stepSize));
			} else {
				containers.put(range, new SpatialLeaf<K, V>());
			}
		}
		return containers.get(range).put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

}
