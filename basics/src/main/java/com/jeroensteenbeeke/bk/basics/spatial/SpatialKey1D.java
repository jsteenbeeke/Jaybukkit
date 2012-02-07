package com.jeroensteenbeeke.bk.basics.spatial;

class SpatialKey1D implements SpatialKey {
	private final long x;

	public SpatialKey1D(long x) {
		super();
		this.x = x;
	}

	public long getX() {
		return x;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (x ^ (x >>> 32));
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
		SpatialKey1D other = (SpatialKey1D) obj;
		if (x != other.x)
			return false;
		return true;
	}

	@Override
	public SpatialRange1D getRange(long stepSize) {
		long baseX = getBase(getX(), stepSize);
		long topX = getX() >= 0 ? baseX + stepSize : baseX - stepSize;

		return new SpatialRange1D(Math.min(baseX, topX), Math.max(baseX, topX));
	}

	protected final long getBase(long real, long stepSize) {
		return stepSize * (real / stepSize);
	}

}
