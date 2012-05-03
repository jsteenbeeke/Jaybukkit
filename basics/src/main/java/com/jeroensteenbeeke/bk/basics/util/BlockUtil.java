package com.jeroensteenbeeke.bk.basics.util;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class BlockUtil {
	private static final BlockFace[] NESW = new BlockFace[] { BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, };

	private static final BlockFace[] NESWUD = new BlockFace[] {
			BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
			BlockFace.UP, BlockFace.DOWN };

	private BlockUtil() {
	}

	public static List<Sign> getAttachedSigns(Block block) {
		List<Sign> signs = Lists.newArrayListWithCapacity(NESW.length);

		for (BlockFace bf : NESW) {
			Block adjacent = block.getRelative(bf);
			BlockState state = adjacent.getState();

			if (state instanceof Sign) {
				signs.add((Sign) state);
			}
		}

		return signs;
	}

	public static List<Block> getAttachedBlocksOfType(Block block,
			Set<Material> targetMaterials) {
		List<Block> blocks = Lists.newArrayListWithCapacity(NESWUD.length);

		for (BlockFace bf : NESWUD) {
			Block adjacent = block.getRelative(bf);
			if (targetMaterials.contains(adjacent.getType())) {
				blocks.add(adjacent);
			}
		}

		return blocks;
	}

	public static List<Sign> getChestSigns(Chest chest) {
		return getSigns(getRelevantChestBlocks(chest));
	}

	private static List<Block> getRelevantChestBlocks(Chest chest) {
		Block block = chest.getBlock();
		List<Block> checkBlocks = Lists.newArrayListWithCapacity(2);
		checkBlocks.add(block);
		checkBlocks.addAll(BlockUtil.getAttachedBlocksOfType(block,
				Sets.newHashSet(Material.CHEST)));
		return checkBlocks;
	}

	private static List<Sign> getSigns(List<Block> checkBlocks) {
		List<Sign> signs = Lists
				.newArrayListWithCapacity(checkBlocks.size() * 4);
		for (Block block : checkBlocks) {
			signs.addAll(BlockUtil.getAttachedSigns(block));
		}
		return signs;
	}

}
