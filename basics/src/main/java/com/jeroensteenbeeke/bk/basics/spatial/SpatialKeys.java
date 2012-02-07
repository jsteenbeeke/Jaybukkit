package com.jeroensteenbeeke.bk.basics.spatial;

public final class SpatialKeys {
	private SpatialKeys() {
	}

	public static SpatialKey keyFor(long x) {
		return new SpatialKey1D(x);
	}

	public static SpatialKey keyFor(long x, long y) {
		return new SpatialKey2D(x, y);
	}

	public static SpatialKey keyFor(long x, long y, long z) {
		return new SpatialKey3D(x, y, z);
	}
}
