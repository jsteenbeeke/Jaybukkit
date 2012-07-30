package com.jeroensteenbeeke.bk.clanblah.builder;

import org.bukkit.Material;
import org.bukkit.block.Block;

public abstract class AbstractBattleshipComponent implements
		BattleshipComponent {
	private final int relativeX;

	private final int relativeY;

	private final int relativeZ;

	protected AbstractBattleshipComponent(int relativeX, int relativeY,
			int relativeZ) {
		super();
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.relativeZ = relativeZ;
	}

	protected final void setRelativeBlock(Block base, int mx, int my, int mz,
			Material type) {
		Block target = getTarget(base, mx, my, mz);
		target.setType(type);
	}

	protected final void setRelativeBlock(Block base, int mx, int my, int mz,
			Material type, byte data, boolean applyPhysics) {
		Block target = getTarget(base, mx, my, mz);
		target.setTypeIdAndData(type.getId(), data, applyPhysics);
	}

	private Block getTarget(Block base, int mx, int my, int mz) {
		final int relX = relativeX + mx;
		final int relY = relativeY + my;
		final int relZ = relativeZ + mz;

		Block target = base.getRelative(relX, relY, relZ);
		return target;
	}
}
