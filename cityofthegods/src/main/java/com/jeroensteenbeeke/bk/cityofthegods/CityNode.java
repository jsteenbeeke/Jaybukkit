package com.jeroensteenbeeke.bk.cityofthegods;

import org.bukkit.Material;

public enum CityNode {
	START {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			if (bottomY == 64) {
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						CityOfTheGodsGenerator.setBlock(result, x, bottomY, z,
								Material.IRON_BLOCK);
						if (x == 0 || x == 15) {
							CityOfTheGodsGenerator.setBlock(result, x,
									bottomY + 1, z, Material.IRON_BLOCK);

						}
						if (z == 0 || z == 15) {
							CityOfTheGodsGenerator.setBlock(result, x,
									bottomY + 1, z, Material.IRON_BLOCK);
						}

					}
				}

				applyGate(result, bottomY, 0, ShapeMode.X);
				applyGate(result, bottomY, 15, ShapeMode.X);
				applyGate(result, bottomY, 0, ShapeMode.Z);
				applyGate(result, bottomY, 15, ShapeMode.Z);
			}
		}
	},
	RANDOM_BUILDING {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			START.generateLevel(result, bottomY, seed, chunkX, chunkZ);

		}
	},
	NORTH_SOUTH {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			if (bottomY == 64) {
				for (int i = 0; i < 16; i++) {
					applyTunnel(result, bottomY, i, ShapeMode.Z);
				}
			}

		}
	},
	EAST_WEST {
		@Override
		public void generateLevel(byte[][] result, int bottomY, long seed,
				int chunkX, int chunkZ) {
			if (bottomY == 64) {
				for (int i = 0; i < 16; i++) {
					applyTunnel(result, bottomY, i, ShapeMode.X);
				}
			}

		}
	};

	private static final int MAX_BOUND = 8;

	public static enum ShapeMode {
		X {
			@Override
			public int determineX(int fixed, int i) {
				return fixed;
			}

			@Override
			public int determineZ(int fixed, int i) {
				return i;
			}
		},
		Z {
			@Override
			public int determineX(int fixed, int i) {
				return i;
			}

			@Override
			public int determineZ(int fixed, int i) {
				return fixed;
			}
		};

		public abstract int determineX(int fixed, int i);

		public abstract int determineZ(int fixed, int i);
	}

	public static CityNode get(int x, int z) {
		final int ax = Math.abs(x);
		final int az = Math.abs(z);
		final boolean xOdd = ax % 2 == 1;
		final boolean zOdd = az % 2 == 1;

		final int lower_max = (3 * MAX_BOUND) / 4;

		if (ax > lower_max) {
			if (az > lower_max)
				return null;
		} else if (az > lower_max) {
			if (ax > lower_max)
				return null;
		}

		if (ax > MAX_BOUND || az > MAX_BOUND)
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

	public void applyGate(byte[][] result, int bottomY, int fixed,
			ShapeMode mode) {
		for (int y = 0; y < 4; y++) {
			for (int i = 6; i <= 9; i++) {
				int x = mode.determineX(fixed, i);
				int z = mode.determineZ(fixed, i);

				Material mat = Material.IRON_BLOCK;

				if (y == 1 || y == 2) {
					if (i == 7 || i == 8)
						mat = Material.AIR;
				}

				CityOfTheGodsGenerator.setBlock(result, x, bottomY + y, z, mat);
			}
		}
	}

	public void applyTunnel(byte[][] result, int bottomY, int fixed,
			ShapeMode mode) {
		for (int y = 0; y < 4; y++) {
			for (int i = 6; i <= 9; i++) {
				int x = mode.determineX(fixed, i);
				int z = mode.determineZ(fixed, i);

				Material mat = Material.GLASS;

				if (y == 1 || y == 2) {
					if (i == 7 || i == 8)
						mat = Material.AIR;
				}

				CityOfTheGodsGenerator.setBlock(result, x, bottomY + y, z, mat);
			}
		}
	}

	public abstract void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ);

}
