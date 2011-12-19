package com.jeroensteenbeeke.bk.ville;

import java.math.BigDecimal;

import org.bukkit.Location;

import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public final class XYZCoordinate {
	private final int x;

	private final int y;

	private final int z;

	public XYZCoordinate(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static XYZCoordinate get(Location location) {
		return new XYZCoordinate(location.getBlockX(), location.getBlockY(),
				location.getBlockZ());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		XYZCoordinate other = (XYZCoordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public int distanceTo(XYZCoordinate other) {
		int xDist = Math.max(getX(), other.getX())
				- Math.min(getX(), other.getX());
		int yDist = Math.max(getY(), other.getY())
				- Math.min(getY(), other.getY());
		int zDist = Math.max(getZ(), other.getZ())
				- Math.min(getZ(), other.getZ());

		BigDecimal xd = new BigDecimal(xDist);
		BigDecimal yd = new BigDecimal(yDist);
		BigDecimal zd = new BigDecimal(zDist);

		BigDecimal x2_z2 = xd.pow(2).add(zd.pow(2));
		BigDecimal xzDist = new BigDecimal(Math.sqrt(x2_z2.doubleValue()));
		BigDecimal xz2_y2 = xzDist.pow(2).add(yd.pow(2));

		return new BigDecimal(Math.sqrt(xz2_y2.doubleValue())).intValue();
	}

	public static XYZCoordinate get(VillageLocation vl) {
		return new XYZCoordinate(vl.getX(), vl.getY(), vl.getZ());
	}

}