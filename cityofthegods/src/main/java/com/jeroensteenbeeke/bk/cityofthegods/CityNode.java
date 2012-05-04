package com.jeroensteenbeeke.bk.cityofthegods;

import org.bukkit.Chunk;
import org.bukkit.Material;

import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil.ShapeMode;

public enum CityNode {
	START {
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

				if (LayoutUtil.isTunnel(chunkX - 1, chunkZ))
					LayoutUtil.applyGate(result, bottomY, 0, ShapeMode.X);
				if (LayoutUtil.isTunnel(chunkX + 1, chunkZ))
					LayoutUtil.applyGate(result, bottomY, 15, ShapeMode.X);
				if (LayoutUtil.isTunnel(chunkX, chunkZ - 1))
					LayoutUtil.applyGate(result, bottomY, 0, ShapeMode.Z);
				if (LayoutUtil.isTunnel(chunkX, chunkZ + 1))
					LayoutUtil.applyGate(result, bottomY, 15, ShapeMode.Z);
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
	},
	RANDOM_BUILDING {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			Building building = Buildings.getRandom(seed, chunkX, chunkZ);

			building.generateLevel(result, bottomY, seed, chunkX, chunkZ);
		}

		@Override
		public void onPopulate(Chunk chunk) {
			Building building = Buildings.getRandom(chunk.getWorld().getSeed(),
					chunk.getX(), chunk.getZ());

			building.onPopulate(chunk);
		}

	},
	NORTH_SOUTH {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			if (bottomY == 64) {
				for (int i = 0; i < 16; i++) {
					LayoutUtil.applyTunnel(result, bottomY, i, ShapeMode.Z);
				}
			}

		}

		@Override
		public void onPopulate(Chunk chunk) {

		}

	},
	EAST_WEST {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			if (bottomY == 64) {
				for (int i = 0; i < 16; i++) {
					LayoutUtil.applyTunnel(result, bottomY, i, ShapeMode.X);
				}
			}

		}

		@Override
		public void onPopulate(Chunk chunk) {

		}
	};

	public static CityNode get(int x, int z) {
		final int ax = Math.abs(x);
		final int az = Math.abs(z);
		final boolean xOdd = ax % 2 == 1;
		final boolean zOdd = az % 2 == 1;

		final int lower_max = (3 * LayoutUtil.MAX_BOUND) / 4;

		if (ax > lower_max) {
			if (az > lower_max)
				return null;
		} else if (az > lower_max) {
			if (ax > lower_max)
				return null;
		}

		if (ax > LayoutUtil.MAX_BOUND || az > LayoutUtil.MAX_BOUND)
			return null;

		if (ax == 0 && az == 0)
			return START;

		if (!xOdd && !zOdd)
			return RANDOM_BUILDING;

		if (xOdd && !zOdd)
			return EAST_WEST;

		if (zOdd && !xOdd)
			return NORTH_SOUTH;

		return null;
	}

	// XXXX3
	// X X2
	// X X1
	// XXXX0
	// 6789

	public abstract void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ);

	public abstract void onPopulate(Chunk chunk);

}
