package com.jeroensteenbeeke.bk.basics.spatial;

class SpatialRange1D implements SpatialRange {
	private final long minX;

	private final long maxX;

	public SpatialRange1D(long minX, long maxX) {
		super();
		this.minX = minX;
		this.maxX = maxX;
	}

	long getMaxX() {
		return maxX;
	}

	long getMinX() {
		return minX;
	}

	@Override
	public boolean contains(SpatialKey key) {
		if (key instanceof SpatialKey1D) {
			SpatialKey1D key1d = (SpatialKey1D) key;

			return minX <= key1d.getX() && maxX >= key1d.getX();
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (maxX ^ (maxX >>> 32));
		result = prime * result + (int) (minX ^ (minX >>> 32));
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
		SpatialRange1D other = (SpatialRange1D) obj;
		if (maxX != other.maxX)
			return false;
		if (minX != other.minX)
			return false;
		return true;
	}

}
