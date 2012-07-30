package com.jeroensteenbeeke.bk.clanblah.builder.components;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.jeroensteenbeeke.bk.clanblah.builder.AbstractBattleshipComponent;

public class RedstoneLineCarrier extends AbstractBattleshipComponent {
	public RedstoneLineCarrier(int relativeX, int relativeY, int relativeZ) {
		super(relativeX, relativeY, relativeZ);
	}

	@Override
	public void construct(Block base) {
		for (int x = -2; x <= 2; x++) {
			for (int z = -3; z <= 3; z++) {
				setRelativeBlock(base, x, 3, z, Material.BEDROCK);
				setRelativeBlock(base, x, 0, z, Material.BEDROCK);
				setRelativeBlock(base, x, -1, z, Material.BEDROCK);
			}
		}

		setRelativeBlock(base, -1, 0, -2, Material.GLOWSTONE);
		setRelativeBlock(base, 1, 0, -2, Material.GLOWSTONE);
		setRelativeBlock(base, -1, 0, 2, Material.GLOWSTONE);
		setRelativeBlock(base, 1, 0, 2, Material.GLOWSTONE);

		for (int z = -3; z < 3; z++) {
			setRelativeBlock(base, -1, 1, z, Material.REDSTONE_WIRE);
		}

		setRelativeBlock(base, -2, 1, -2, Material.REDSTONE_WIRE);
		setRelativeBlock(base, 2, 1, -2, Material.REDSTONE_WIRE);

		setRelativeBlock(base, -2, 1, -1, Material.BEDROCK);
		setRelativeBlock(base, 2, 1, -1, Material.BEDROCK);

		setRelativeBlock(base, -2, 1, 0, Material.BEDROCK);
		setRelativeBlock(base, 2, 1, 0, Material.BEDROCK);

		setRelativeBlock(base, -2, 1, -2, Material.REDSTONE_WIRE);
		setRelativeBlock(base, 2, 1, -2, Material.REDSTONE_WIRE);
	}

	@Override
	public void deconstruct(Block battleshipLocation) {
		// TODO Auto-generated method stub

	}

}
