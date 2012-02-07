package com.jeroensteenbeeke.bk.basics.spatial;

class SpatialRange3D extends SpatialRange2D {
	private final long minZ;

	private final long maxZ;

	public SpatialRange3D(long minX, long maxX, long minY, long maxY,
			long minZ, long maxZ) {
		super(minX, maxX, minY, maxY);
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	long getMinZ() {
		return minZ;
	}

	long getMaxZ() {
		return maxZ;
	}

	@Override
	public boolean contains(SpatialKey key) {
		if (key instanceof SpatialKey3D) {
			SpatialKey3D key3d = (SpatialKey3D) key;

			return minZ <= key3d.getZ() && maxZ >= key3d.getZ()
					&& super.contains(key);
		}

		return false;
	}

}
