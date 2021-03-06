package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;

public class Forum extends Building {

	@Override
	public void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		if (bottomY == 64) {
			for (int i = 0; i <= 2; i++) {
				horizontalRect(result, bottomY, i, i, 15 - i, 15 - i);
			}
			horizontalRect(result, bottomY + 1, 0, 0, 15, 15);

			applyTunnelEntrances(result, bottomY, chunkX, chunkZ);

		} else if (bottomY == 48) {
			horizontalRect(result, bottomY + 15, 3, 3, 12, 12);
			horizontalRect(result, bottomY + 15, 4, 4, 11, 11);
			horizontalRect(result, bottomY + 14, 5, 5, 10, 10);
			horizontalRect(result, bottomY + 14, 6, 6, 9, 9);
			horizontalRect(result, bottomY + 13, 7, 7, 8, 8);
			LayoutUtil.setBlock(result, 7, bottomY + 14, 7, Material.GLOWSTONE);
			LayoutUtil.setBlock(result, 7, bottomY + 14, 8, Material.GLOWSTONE);
			LayoutUtil.setBlock(result, 8, bottomY + 14, 7, Material.GLOWSTONE);
			LayoutUtil.setBlock(result, 8, bottomY + 14, 8, Material.GLOWSTONE);
		}

	}

	@Override
	public void onPopulate(Chunk chunk) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				if (z == 0 || z == 15) {
					if (x == 2 || x == 5 || x == 13 || x == 10)
						chunk.getBlock(x, 66, z).setType(Material.TORCH);
				}

				if (x == 0 || x == 15) {
					if (z == 2 || z == 5 || z == 13 || z == 10)
						chunk.getBlock(x, 66, z).setType(Material.TORCH);
				}
			}
		}
	}

}
