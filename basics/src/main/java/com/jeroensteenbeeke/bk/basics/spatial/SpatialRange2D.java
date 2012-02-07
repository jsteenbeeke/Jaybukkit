package com.jeroensteenbeeke.bk.basics.spatial;

class SpatialRange2D extends SpatialRange1D {

	private final long minY;

	private final long maxY;

	public SpatialRange2D(long minX, long maxX, long minY, long maxY) {
		super(minX, maxX);
		this.minY = minY;
		this.maxY = maxY;
	}

	long getMaxY() {
		return maxY;
	}

	long getMinY() {
		return minY;
	}

	@Override
	public boolean contains(SpatialKey key) {
		if (key instanceof SpatialKey2D) {
			SpatialKey2D key2d = (SpatialKey2D) key;

			return minY <= key2d.getY() && maxY >= key2d.getY()
					&& super.contains(key);
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (maxY ^ (maxY >>> 32));
		result = prime * result + (int) (minY ^ (minY >>> 32));
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
		SpatialRange2D other = (SpatialRange2D) obj;
		if (maxY != other.maxY)
			return false;
		if (minY != other.minY)
			return false;
		return true;
	}

}
