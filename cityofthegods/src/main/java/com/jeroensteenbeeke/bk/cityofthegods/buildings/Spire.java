package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;

public class Spire extends Building {
	@Override
	public void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		if (bottomY == 48)
			renderBottom(result, bottomY, seed, chunkX, chunkZ);

		if (bottomY == 64)
			renderEntrance(result, bottomY, seed, chunkX, chunkZ);

		if (bottomY == 80)
			renderRoof(result, bottomY, seed, chunkX, chunkZ);

	}

	private void renderRoof(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		for (int x = 7; x <= 8; x++) {
			for (int z = 7; z <= 8; z++) {
				for (int y = 0; y < 2; y++) {
					LayoutUtil.setBlock(result, x, bottomY + y, z,
							Material.SANDSTONE);
				}
			}
		}

	}

	private void renderBottom(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				drawIfFill(result, bottomY, x, z, 14, 16, 1, 14);
				drawIfFill(result, bottomY, x, z, 12, 14, 2, 13);
				drawIfFill(result, bottomY, x, z, 10, 12, 3, 12);
				drawIfFill(result, bottomY, x, z, 8, 10, 4, 11);
				drawIfFill(result, bottomY, x, z, 6, 8, 5, 10);
				drawIfFill(result, bottomY, x, z, 4, 6, 6, 9);
				drawIfFill(result, bottomY, x, z, 2, 4, 7, 8);
			}
		}

	}

	private void renderEntrance(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				LayoutUtil.setBlock(result, x, bottomY, z, Material.SANDSTONE);

				for (int y = 1; y < 4; y++) {
					if (x == 0 || x == 15) {
						LayoutUtil.setBlock(result, x, bottomY + y, z,
								Material.SANDSTONE);

					}
					if (z == 0 || z == 15) {
						LayoutUtil.setBlock(result, x, bottomY + y, z,
								Material.SANDSTONE);
					}
				}

				drawIfContainingRect(result, bottomY, x, z, 4, 6, 1, 14);
				drawIfContainingRect(result, bottomY, x, z, 6, 8, 2, 13);
				drawIfContainingRect(result, bottomY, x, z, 8, 10, 3, 12);
				drawIfContainingRect(result, bottomY, x, z, 10, 12, 4, 11);
				drawIfContainingRect(result, bottomY, x, z, 12, 14, 5, 10);
				drawIfContainingRect(result, bottomY, x, z, 14, 16, 6, 9);

			}
		}

		applyTunnelEntrances(result, bottomY, chunkX, chunkZ);

	}

	@Override
	public void onPopulate(Chunk chunk) {
		placeTorch(chunk, 1, 1, 66, 2, 13, 5, 10);
		placeTorch(chunk, 14, 14, 66, 2, 13, 5, 10);

		placeTorch(chunk, 2, 2, 68, 3, 12, 6, 9);
		placeTorch(chunk, 13, 13, 68, 3, 12, 6, 9);

		placeTorch(chunk, 3, 3, 70, 4, 11, 6, 9);
		placeTorch(chunk, 12, 12, 70, 4, 11, 6, 9);

		placeTorch(chunk, 4, 4, 72, 6, 9);
		placeTorch(chunk, 11, 11, 72, 6, 9);

		placeTorch(chunk, 5, 5, 74, 6, 9);
		placeTorch(chunk, 10, 10, 74, 6, 9);

		for (int x = 7; x <= 8; x++)
			for (int z = 7; z <= 8; z++) {
				chunk.getBlock(x, 65, z).setType(Material.SANDSTONE);
				chunk.getBlock(x, 66, z).setType(Material.TORCH);
			}
	}
}
