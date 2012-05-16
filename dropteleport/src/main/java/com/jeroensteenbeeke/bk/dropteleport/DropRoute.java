package com.jeroensteenbeeke.bk.dropteleport;

import org.bukkit.World;

public final class DropRoute {
	private final World targetWorld;

	private final int maxHeight;

	public DropRoute(World targetWorld, int maxHeight) {
		super();
		this.targetWorld = targetWorld;
		this.maxHeight = maxHeight;
	}

	public World getTargetWorld() {
		return targetWorld;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxHeight;
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
		DropRoute other = (DropRoute) obj;
		if (maxHeight != other.maxHeight)
			return false;
		return true;
	}

}
