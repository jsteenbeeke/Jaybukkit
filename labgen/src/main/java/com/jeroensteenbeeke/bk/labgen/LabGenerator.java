package com.jeroensteenbeeke.bk.labgen;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class LabGenerator extends ChunkGenerator {
	private static final int MAX = 15;

	static enum Mode {
		TOP_LEFT {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (xx == 0 || zz == MAX)
					return id(Material.BEDROCK);

				if (xx == 1 || zz == MAX - 1)
					return id(Material.IRON_BLOCK);

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		TOP {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (zz == MAX) {
					if (in(yy, 52, 53) && in(xx, 7, 8))
						return id(Material.AIR);

					if (in(yy, 51, 54) && in(xx, 6, 7, 8, 9))
						return id(Material.IRON_BLOCK);

					if (in(yy, 52, 53) && in(xx, 6, 9))
						return id(Material.IRON_BLOCK);

					return id(Material.BEDROCK);
				} else if (zz == MAX - 1) {
					if (in(yy, 52, 53) && in(xx, 7, 8))
						return id(Material.AIR);

					return id(Material.IRON_BLOCK);
				}

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		TOP_RIGHT {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy == 0 || yy == 1)
					return id(Material.BEDROCK);

				if (xx == MAX || zz == MAX)
					return id(Material.BEDROCK);

				if (xx == MAX - 1 || zz == MAX - 1)
					return id(Material.IRON_BLOCK);

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		LEFT {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (xx == 0) {
					if (in(yy, 52, 53) && in(zz, 7, 8))
						return id(Material.AIR);

					if (in(yy, 51, 54) && in(zz, 6, 7, 8, 9))
						return id(Material.IRON_BLOCK);

					if (in(yy, 52, 53) && in(zz, 6, 9))
						return id(Material.IRON_BLOCK);

					return id(Material.BEDROCK);
				} else if (xx == 1) {
					if (in(yy, 52, 53) && in(zz, 7, 8))
						return id(Material.AIR);

					return id(Material.IRON_BLOCK);
				}

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		CENTER {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (yy == 51)
					return id(Material.IRON_BLOCK);

				return 0;
			}

		},
		RIGHT {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (xx == MAX) {
					if (in(yy, 52, 53) && in(zz, 7, 8))
						return id(Material.AIR);

					if (in(yy, 51, 54) && in(zz, 6, 7, 8, 9))
						return id(Material.IRON_BLOCK);

					if (in(yy, 52, 53) && in(zz, 6, 9))
						return id(Material.IRON_BLOCK);

					return id(Material.BEDROCK);
				} else if (xx == MAX - 1) {
					if (in(yy, 52, 53) && in(zz, 7, 8))
						return id(Material.AIR);

					return id(Material.IRON_BLOCK);
				}

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		BOTTOM_LEFT {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (xx == 0 || zz == 0)
					return id(Material.BEDROCK);

				if (xx == 1 || zz == 1)
					return id(Material.IRON_BLOCK);

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		BOTTOM {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (zz == 0) {
					if (in(yy, 52, 53) && in(xx, 7, 8))
						return id(Material.AIR);

					if (in(yy, 51, 54) && in(xx, 6, 7, 8, 9))
						return id(Material.IRON_BLOCK);

					if (in(yy, 52, 53) && in(xx, 6, 9))
						return id(Material.IRON_BLOCK);

					return id(Material.BEDROCK);
				} else if (zz == 1) {
					if (in(yy, 52, 53) && in(xx, 7, 8))
						return id(Material.AIR);

					return id(Material.IRON_BLOCK);
				}

				return CENTER.getBlock(xx, yy, zz);
			}

		},
		BOTTOM_RIGHT {
			@Override
			public byte getBlock(int xx, int yy, int zz) {
				if (yy <= 50)
					return id(Material.BEDROCK);

				if (xx == MAX || zz == 0)
					return id(Material.BEDROCK);

				if (xx == MAX - 1 || zz == 1)
					return id(Material.IRON_BLOCK);

				return CENTER.getBlock(xx, yy, zz);
			}

		};

		public static Mode getMode(int x, int z) {
			int modx = formula(x);
			int modz = formula(z);

			switch (modx) {
			case 0:
				switch (modz) {
				case 0:
					return BOTTOM_LEFT;
				case 1:
					return LEFT;
				case 2:
					return LEFT;
				case 3:
					return TOP_LEFT;
				}
			case 1:
			case 2:
				switch (modz) {
				case 0:
					return BOTTOM;
				case 1:
					return CENTER;
				case 2:
					return CENTER;
				case 3:
					return TOP;
				}

			case 3:
				switch (modz) {
				case 0:
					return BOTTOM_RIGHT;
				case 1:
					return RIGHT;
				case 2:
					return RIGHT;
				case 3:
					return TOP_RIGHT;
				}
			}

			return CENTER;
		}

		public abstract byte getBlock(int xx, int yy, int zz);

		protected boolean in(int base, int... values) {
			for (int v : values) {
				if (base == v)
					return true;
			}

			return false;
		}

		protected byte id(Material m) {
			return (byte) m.getId();
		}

		static int formula(int base) {
			return base >= 0 ? base % 4 : fourify(base);
		}

		static int fourify(int base) {
			int rv = base;

			while (rv < 0)
				rv += 4;

			return rv;
		}

	}

	@Override
	public byte[] generate(World world, Random random, int x, int z) {
		byte[] result = new byte[32768];

		Mode mode = Mode.getMode(x, z);

		for (int xx = 0; xx < 16; xx++) {
			for (int zz = 0; zz < 16; zz++) {
				for (int yy = 0; yy < 128; yy++) {
					result[(xx * 16 + zz) * 128 + yy] = mode.getBlock(xx, yy,
							zz);
				}
			}
		}
		return result;
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {

		return new Location(world, 32, 4, 32);
	}

}
