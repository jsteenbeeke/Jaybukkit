package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;

public class FlowerPatch extends Building {

	@Override
	public void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		if (bottomY == 64) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					LayoutUtil.setBlock(result, x, bottomY, z,
							LayoutUtil.BASE_MATERIAL);
				}
			}

			horizontalRect(result, bottomY + 1, 0, 0, 15, 15);

			horizontalRect(result, bottomY + 1, 0, 0, 5, 5);
			horizontalRect(result, bottomY + 1, 10, 0, 15, 5);
			horizontalRect(result, bottomY + 1, 0, 10, 5, 15);
			horizontalRect(result, bottomY + 1, 10, 10, 15, 15);

			horizontalRect(result, bottomY + 1, 1, 1, 4, 4, Material.GRASS);
			horizontalRect(result, bottomY + 1, 2, 2, 3, 3, Material.GRASS);
			horizontalRect(result, bottomY + 2, 1, 1, 4, 4, Material.RED_ROSE);
			horizontalRect(result, bottomY + 2, 2, 2, 3, 3,
					Material.YELLOW_FLOWER);

			horizontalRect(result, bottomY + 1, 11, 1, 14, 4, Material.GRASS);
			horizontalRect(result, bottomY + 1, 12, 2, 13, 3, Material.GRASS);
			horizontalRect(result, bottomY + 2, 11, 1, 14, 4,
					Material.YELLOW_FLOWER);
			horizontalRect(result, bottomY + 2, 12, 2, 13, 3, Material.RED_ROSE);

			horizontalRect(result, bottomY + 1, 1, 11, 4, 14, Material.GRASS);
			horizontalRect(result, bottomY + 1, 2, 12, 3, 13, Material.GRASS);
			horizontalRect(result, bottomY + 2, 1, 11, 4, 14,
					Material.YELLOW_FLOWER);
			horizontalRect(result, bottomY + 2, 2, 12, 3, 13, Material.RED_ROSE);

			horizontalRect(result, bottomY + 1, 11, 11, 14, 14, Material.GRASS);
			horizontalRect(result, bottomY + 1, 12, 12, 13, 13, Material.GRASS);
			horizontalRect(result, bottomY + 2, 11, 11, 14, 14,
					Material.RED_ROSE);
			horizontalRect(result, bottomY + 2, 12, 12, 13, 13,
					Material.YELLOW_FLOWER);

			applyTunnelEntrances(result, bottomY, chunkX, chunkZ);
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
