package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;

public class FourHouse extends Building {
	@Override
	public void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		if (bottomY == 48)
			renderBottom(result, bottomY, seed, chunkX, chunkZ);

		if (bottomY == 64)
			renderEntrance(result, bottomY, seed, chunkX, chunkZ);

	}

	private void renderEntrance(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				LayoutUtil.setBlock(result, x, bottomY, z,
						LayoutUtil.BASE_MATERIAL);
			}
		}

		horizontalRect(result, bottomY + 1, 0, 0, 15, 15);

		for (int y = 1; y <= 4; y++) {
			horizontalRect(result, bottomY + y, 0, 0, 5, 5);
			horizontalRect(result, bottomY + y, 10, 0, 15, 5);
			horizontalRect(result, bottomY + y, 0, 10, 5, 15);
			horizontalRect(result, bottomY + y, 10, 10, 15, 15);
		}

		horizontalRect(result, bottomY + 5, 1, 1, 4, 4);
		horizontalRect(result, bottomY + 5, 2, 2, 3, 3);

		horizontalRect(result, bottomY + 5, 11, 1, 14, 4);
		horizontalRect(result, bottomY + 5, 12, 2, 13, 3);

		horizontalRect(result, bottomY + 5, 1, 11, 4, 14);
		horizontalRect(result, bottomY + 5, 2, 12, 3, 13);

		horizontalRect(result, bottomY + 5, 11, 11, 14, 14);
		horizontalRect(result, bottomY + 5, 12, 12, 13, 13);

		applyTunnelEntrances(result, bottomY, chunkX, chunkZ);

	}

	// WEST
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|SS|SS|SS|SS|SS|--|--|--|--|SS|SS|SS|SS|SS|SS| 15
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|TT|--|--|TT|SS|--|--|--|TT|SS|TT|--|--|TT|SS| 14
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|--|--|--|--|SS|--|--|--|--|WD|--|--|--|--|SS| 13
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|--|--|--|--|SS|--|--|--|TT|SS|TT|--|--|--|SS| 12
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|TT|--|TT|--|SS|--|--|--|--|SS|--|--|--|TT|SS| 11
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|SS|WD|SS|SS|SS|--|--|--|--|SS|SS|SS|SS|SS|SS| 10
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |--|TT|--|TT|--|--|--|--|--|--|--|--|--|--|--|--| 9
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 8
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ SOUTH
	// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 7
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |--|--|--|--|--|--|--|--|--|--|--|--|TT|--|TT|--| 6
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|SS|SS|SS|SS|SS|--|--|--|--|SS|SS|SS|WD|SS|SS| 5
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|TT|--|--|--|SS|--|--|--|--|SS|--|TT|--|TT|SS| 4
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|--|--|--|TT|SS|TT|--|--|--|SS|--|--|--|--|SS| 3
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|--|--|--|--|WD|--|--|--|--|SS|--|--|--|--|SS| 2
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|TT|--|--|TT|SS|TT|--|--|--|SS|TT|--|--|TT|SS| 1
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |SS|SS|SS|SS|SS|SS|--|--|--|--|SS|SS|SS|SS|SS|SS| 0
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// |00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15| ^-- Z
	// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	// ----X---->

	@Override
	public void onPopulate(Chunk chunk) {
		doorAt(chunk, 10, 65, 13, Material.WOODEN_DOOR, BlockFace.NORTH);
		torchAt(chunk, 9, 66, 12, BlockFace.NORTH);
		torchAt(chunk, 9, 66, 14, BlockFace.NORTH);
		torchAt(chunk, 11, 66, 12, BlockFace.SOUTH);
		torchAt(chunk, 11, 66, 14, BlockFace.SOUTH);
		creatureAt(chunk, 9, 66, 13, EntityType.VILLAGER);

		doorAt(chunk, 2, 65, 10, Material.WOODEN_DOOR, BlockFace.EAST);
		torchAt(chunk, 1, 66, 9, BlockFace.EAST);
		torchAt(chunk, 3, 66, 9, BlockFace.EAST);
		torchAt(chunk, 1, 66, 11, BlockFace.WEST);
		torchAt(chunk, 3, 66, 11, BlockFace.WEST);
		creatureAt(chunk, 2, 66, 9, EntityType.VILLAGER);

		doorAt(chunk, 5, 65, 2, Material.WOODEN_DOOR, BlockFace.SOUTH);
		torchAt(chunk, 6, 66, 1, BlockFace.SOUTH);
		torchAt(chunk, 6, 66, 3, BlockFace.SOUTH);
		torchAt(chunk, 4, 66, 1, BlockFace.NORTH);
		torchAt(chunk, 4, 66, 3, BlockFace.NORTH);
		creatureAt(chunk, 6, 66, 2, EntityType.VILLAGER);

		doorAt(chunk, 13, 65, 5, Material.WOODEN_DOOR, BlockFace.WEST);
		torchAt(chunk, 12, 66, 6, BlockFace.WEST);
		torchAt(chunk, 14, 66, 6, BlockFace.WEST);
		torchAt(chunk, 12, 66, 4, BlockFace.EAST);
		torchAt(chunk, 14, 66, 4, BlockFace.EAST);
		creatureAt(chunk, 13, 66, 6, EntityType.VILLAGER);

	}

}
