package com.jeroensteenbeeke.bk.basics.spatial;

import java.util.HashMap;
import java.util.Map;

class SpatialLeaf<K extends SpatialKey, V> extends HashMap<K, V> implements
		SpatialContainer<K, V> {
	private static final long serialVersionUID = 1L;

	public SpatialLeaf() {
		super();
	}

	public SpatialLeaf(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public SpatialLeaf(int initialCapacity) {
		super(initialCapacity);
	}

	public SpatialLeaf(Map<? extends K, ? extends V> m) {
		super(m);
	}

}
