package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;

//WEST
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 15
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 14
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 13
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 12
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 11
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|SSSSSSSSSSSSSSSSSSSSSSSSSSSSS|--|--|--| 10
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 9
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 8
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ SOUTH
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 7
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 6
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|SSSSSSSSSSSSSSSSSSSSSSSSSSSSS|--|--|--| 5
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 4
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 3
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 2
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 1
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 0
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15| ^--- Z
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//-----X--->
public class Mansion extends Building {

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

		for (int y = 1; y <= 5; y++) {
			horizontalRect(result, bottomY + y, 3, 5, 12, 10);
		}

		horizontalRect(result, bottomY + 6, 4, 6, 11, 9);
		horizontalRect(result, bottomY + 7, 5, 7, 10, 8);
		horizontalRect(result, bottomY + 7, 6, 7, 9, 8);
		horizontalRect(result, bottomY + 7, 7, 7, 8, 8);

		applyTunnelEntrances(result, bottomY, chunkX, chunkZ);
	}

	@Override
	public void onPopulate(Chunk chunk) {
		doorAt(chunk, 7, 65, 5, Material.WOODEN_DOOR, BlockFace.EAST);
		torchAt(chunk, 6, 66, 4, BlockFace.EAST);
		torchAt(chunk, 6, 66, 6, BlockFace.WEST);

		doorAt(chunk, 8, 65, 5, Material.WOODEN_DOOR, BlockFace.EAST);
		torchAt(chunk, 9, 66, 4, BlockFace.EAST);
		torchAt(chunk, 9, 66, 6, BlockFace.WEST);
	}

}
