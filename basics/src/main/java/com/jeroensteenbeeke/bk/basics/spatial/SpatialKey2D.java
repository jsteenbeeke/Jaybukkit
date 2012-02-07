package com.jeroensteenbeeke.bk.basics.spatial;

class SpatialKey2D extends SpatialKey1D {
	private final long y;

	public SpatialKey2D(long x, long y) {
		super(x);
		this.y = y;
	}

	public long getY() {
		return y;
	}

	@Override
	public SpatialRange2D getRange(long stepSize) {
		long baseX = getBase(getX(), stepSize);
		long topX = getX() >= 0 ? baseX + stepSize : baseX - stepSize;

		long baseY = getBase(getY(), stepSize);
		long topY = getY() >= 0 ? baseY + stepSize : baseY - stepSize;

		return new SpatialRange2D(Math.min(baseX, topX), Math.max(baseX, topX),
				Math.min(baseY, topY), Math.max(baseY, topY));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (y ^ (y >>> 32));
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
		SpatialKey2D other = (SpatialKey2D) obj;
		if (y != other.y)
			return false;
		return true;
	}
}
