package com.jeroensteenbeeke.bk.basics.spatial;

class SpatialKey3D extends SpatialKey2D {
	private final long z;

	public SpatialKey3D(long x, long y, long z) {
		super(x, y);
		this.z = z;
	}

	public long getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (z ^ (z >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpatialKey3D other = (SpatialKey3D) obj;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public SpatialRange3D getRange(long stepSize) {
		long baseX = getBase(getX(), stepSize);
		long topX = getX() >= 0 ? baseX + stepSize : baseX - stepSize;

		long baseY = getBase(getY(), stepSize);
		long topY = getY() >= 0 ? baseY + stepSize : baseY - stepSize;

		long baseZ = getBase(getZ(), stepSize);
		long topZ = getZ() >= 0 ? baseZ + stepSize : baseZ - stepSize;

		return new SpatialRange3D(Math.min(baseX, topX), Math.max(baseX, topX),
				Math.min(baseY, topY), Math.max(baseY, topY), Math.min(baseZ,
						topZ), Math.max(baseZ, topZ));
	}
}
