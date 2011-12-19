package com.jeroensteenbeeke.bk.ville;

public class SpatialIndex {
	private final int minX;

	private final int maxX;

	private final int minY;

	private final int maxY;

	private final int minZ;

	private final int maxZ;

	public SpatialIndex(int radius, int x, int y, int z) {
		this.minX = x - radius;
		this.maxX = x - radius;
		this.minY = y - radius;
		this.maxY = y - radius;
		this.minZ = z - radius;
		this.maxZ = z - radius;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public boolean contains(XYZCoordinate xyz) {
		if (xyz.getX() < minX || xyz.getY() > maxX)
			return false;

		if (xyz.getY() < minY || xyz.getY() > maxY)
			return false;

		if (xyz.getZ() < minZ || xyz.getZ() > maxZ)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxX;
		result = prime * result + maxY;
		result = prime * result + maxZ;
		result = prime * result + minX;
		result = prime * result + minY;
		result = prime * result + minZ;
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
		SpatialIndex other = (SpatialIndex) obj;
		if (maxX != other.maxX)
			return false;
		if (maxY != other.maxY)
			return false;
		if (maxZ != other.maxZ)
			return false;
		if (minX != other.minX)
			return false;
		if (minY != other.minY)
			return false;
		if (minZ != other.minZ)
			return false;
		return true;
	}

}
