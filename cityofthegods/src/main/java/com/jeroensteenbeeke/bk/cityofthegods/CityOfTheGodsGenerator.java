package com.jeroensteenbeeke.bk.cityofthegods;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

//
//   -3 -2 -1 00 01 02 03 04
//-3
//-2    RB EW RB EW RB EW RB
//-1    NS    NS    NS    NS
//00    RB EW SS EW RB EW RB
//01    NS    NS    NS    NS
//02    RB EW RB EW RB EW RB
//03

public class CityOfTheGodsGenerator extends ChunkGenerator {

	static void setBlock(byte[][] result, int x, int y, int z, Material block) {
		byte mat = (byte) block.getId();

		if (result[y >> 4] == null) {
			result[y >> 4] = new byte[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = mat;
	}

	@Override
	public byte[][] generateBlockSections(World world, Random random, int x,
			int z, BiomeGrid biomes) {
		int levels = world.getMaxHeight() / 16;

		byte[][] result = new byte[levels][4096];

		if (levels < 7) {
			// Generate empty plain of bedrock
			for (int xx = 0; x < 16; x++) {
				for (int zz = 0; z < 16; z++) {
					setBlock(result, xx, 61, zz, Material.BEDROCK);
					setBlock(result, xx, 62, zz, Material.DIRT);
					setBlock(result, xx, 63, zz, Material.GRASS);
				}
			}
		}

		CityNode node = CityNode.get(x, z);

		if (node != null) {

			for (int level = 0; level < levels; level++) {
				node.generateLevel(result, level * 16, world.getSeed(), x, z);
			}
		}

		return result;
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return new Location(world, 8, 65, 8);
	}
}