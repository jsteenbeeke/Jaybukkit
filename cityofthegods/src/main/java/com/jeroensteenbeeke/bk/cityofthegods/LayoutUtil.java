package com.jeroensteenbeeke.bk.cityofthegods;

import org.bukkit.Material;

public final class LayoutUtil {
	private LayoutUtil() {
	}

	public static final byte TORCH_POINTING_EAST = (byte) 0x4;

	public static final byte TORCH_POINTING_WEST = (byte) 0x3;

	public static final byte TORCH_POINTING_SOUTH = (byte) 0x1;

	public static final byte TORCH_POINTING_NORTH = (byte) 0x2;

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

	public static void applyTunnel(byte[][] result, int bottomY, int fixed,
			ShapeMode mode) {
		final int MIN_Y = 0;
		final int MAX_Y = 3;

		final int MIN_I = 6;
		final int MAX_I = 9;

		for (int y = MIN_Y; y <= 1; y++) {
			for (int i = MIN_I; i <= MAX_I; i++) {
				int x = mode.determineX(fixed, i);
				int z = mode.determineZ(fixed, i);

				Material mat = Material.SANDSTONE;

				if (y > MIN_Y) {
					if (i > MIN_I && i < MAX_I) {
						mat = Material.AIR;
					}
				}

				if (i == MIN_I || i == MAX_I) {
					if (y == MIN_Y || y == MAX_Y)
						mat = Material.AIR;
				}

				setBlock(result, x, bottomY + y, z, mat);
			}
		}
	}

	protected boolean isBuilding(int chunkX, int chunkZ) {
		final int ax = Math.abs(chunkX);
		final int az = Math.abs(chunkZ);
		final boolean xOdd = ax % 2 == 1;
		final boolean zOdd = az % 2 == 1;

		final int lower_max = (3 * MAX_BOUND) / 4;

		if (ax > lower_max) {
			if (az > lower_max)
				return false;
		} else if (az > lower_max) {
			if (ax > lower_max)
				return false;
		}

		if (ax > MAX_BOUND || az > MAX_BOUND)
			return false;

		if (ax == 0 && az == 0)
			return true;

		if (!xOdd && !zOdd)
			return true;

		if (xOdd && !zOdd)
			return false;

		if (zOdd && !xOdd)
			return false;

		return false;
	}

	public static boolean isTunnel(int chunkX, int chunkZ) {
		final int ax = Math.abs(chunkX);
		final int az = Math.abs(chunkZ);
		final boolean xOdd = ax % 2 == 1;
		final boolean zOdd = az % 2 == 1;

		final int lower_max = (3 * MAX_BOUND) / 4;

		if (ax > lower_max) {
			if (az > lower_max)
				return false;
		} else if (az > lower_max) {
			if (ax > lower_max)
				return false;
		}

		if (ax > MAX_BOUND || az > MAX_BOUND)
			return false;

		if (ax == 0 && az == 0)
			return false;

		if (!xOdd && !zOdd)
			return false;

		if (xOdd && !zOdd)
			return true;

		if (zOdd && !xOdd)
			return true;

		return false;
	}

	public static void applyGate(byte[][] result, int bottomY, int fixed,
			ShapeMode mode) {
		for (int y = 0; y < 4; y++) {
			for (int i = 6; i <= 9; i++) {
				int x = mode.determineX(fixed, i);
				int z = mode.determineZ(fixed, i);

				Material mat = Material.SANDSTONE;

				if (y == 1 || y == 2) {
					if (i == 7 || i == 8)
						mat = Material.AIR;
				}

				setBlock(result, x, bottomY + y, z, mat);
			}
		}
	}

	public static void setBlock(byte[][] result, int x, int y, int z,
			Material block) {
		byte mat = (byte) block.getId();

		if (result[y >> 4] == null) {
			result[y >> 4] = new byte[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = mat;
	}

	public static final int MAX_BOUND = 8;
}
