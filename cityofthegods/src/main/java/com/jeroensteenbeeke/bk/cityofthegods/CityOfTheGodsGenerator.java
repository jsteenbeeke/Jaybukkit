package com.jeroensteenbeeke.bk.cityofthegods;

import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import com.google.common.collect.Lists;

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

	@Override
	public byte[][] generateBlockSections(World world, Random random, int x,
			int z, BiomeGrid biomes) {
		int levels = world.getMaxHeight() / 16;

		byte[][] result = new byte[levels][4096];

		if (levels < 7) {
			// Generate empty plain of bedrock
			for (int xx = 0; x < 16; x++) {
				for (int zz = 0; z < 16; z++) {
					LayoutUtil.setBlock(result, xx, 61, zz, Material.BEDROCK);
					LayoutUtil.setBlock(result, xx, 62, zz, Material.DIRT);
					LayoutUtil.setBlock(result, xx, 63, zz, Material.GRASS);
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
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Lists.<BlockPopulator> newArrayList(new BlockPopulator() {
			@Override
			public void populate(World world, Random random, Chunk source) {
				CityNode node = CityNode.get(source.getX(), source.getZ());

				if (node != null) {
					node.onPopulate(source);
				}

			}
		});
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return new Location(world, 8, 65, 8);
	}
}