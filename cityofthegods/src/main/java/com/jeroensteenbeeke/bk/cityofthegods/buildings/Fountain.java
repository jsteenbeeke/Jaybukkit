package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;

public class Fountain extends Building {
	@Override
	public void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		if (bottomY == 64) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					LayoutUtil.setBlock(result, x, bottomY, z,
							Material.SANDSTONE);
					if (x == 0 || x == 15) {
						LayoutUtil.setBlock(result, x, bottomY + 1, z,
								Material.SANDSTONE);

					}
					if (z == 0 || z == 15) {
						LayoutUtil.setBlock(result, x, bottomY + 1, z,
								Material.SANDSTONE);
					}

				}
			}

			for (int x = 7; x <= 8; x++) {
				for (int z = 7; z <= 8; z++) {
					LayoutUtil.setBlock(result, x, bottomY + 2, z,
							Material.SANDSTONE);
				}
			}

			for (int x = 7; x <= 8; x++) {
				for (int z = 7; z <= 8; z++) {
					LayoutUtil.setBlock(result, x, bottomY + 3, z,
							Material.WATER);
				}
			}

			for (int x = 6; x <= 9; x++) {
				for (int z = 6; z <= 9; z++) {
					LayoutUtil.setBlock(result, x, bottomY + 1, z,
							Material.SANDSTONE);
				}
			}

			for (int x = 5; x <= 10; x++) {
				for (int z = 5; z <= 10; z++) {
					if (x == 5 || x == 10)
						if (z != 8 && z != 7)
							LayoutUtil.setBlock(result, x, bottomY, z,
									Material.AIR);

					if (z == 5 || z == 10)
						if (x != 8 && x != 7)
							LayoutUtil.setBlock(result, x, bottomY, z,
									Material.AIR);

				}
			}

			for (int x = 4; x <= 11; x++) {
				for (int z = 4; z <= 11; z++) {
					if (x == 4 || x == 11 || z == 4 || z == 11)
						LayoutUtil.setBlock(result, x, bottomY + 1, z,
								Material.SANDSTONE);

				}
			}

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
