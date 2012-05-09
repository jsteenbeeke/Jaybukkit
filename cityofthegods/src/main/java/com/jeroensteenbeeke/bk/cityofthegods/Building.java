package com.jeroensteenbeeke.bk.cityofthegods;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;
import org.bukkit.material.Ladder;
import org.bukkit.material.Torch;

import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil.ShapeMode;

//WEST
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 15
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 14
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 13
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 12
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 11
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 10
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 9
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 8
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ SOUTH
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 7
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 6
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 5
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 4
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 3
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 2
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 1
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 0
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// |00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15| ^--- Z
// +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
// -----X--->

public abstract class Building {
	public abstract void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ);

	public abstract void onPopulate(Chunk chunk);

	protected void drawIfContainingRect(byte[][] result, int bottomY, int x,
			int z, int minY, int maxY, int rangeMin, int rangeMax) {
		for (int y = minY; y < maxY; y++) {
			if (x == rangeMin || x == rangeMax) {
				if (z >= rangeMin && z <= rangeMax)
					LayoutUtil.setBlock(result, x, bottomY + y, z,
							LayoutUtil.BASE_MATERIAL);

			}
			if (z == rangeMin || z == rangeMax) {
				if (x >= rangeMin && x <= rangeMax)
					LayoutUtil.setBlock(result, x, bottomY + y, z,
							LayoutUtil.BASE_MATERIAL);
			}
		}
	}

	protected void horizontalRect(byte[][] result, int y, int minX, int minZ,
			int maxX, int maxZ) {
		horizontalRect(result, y, minX, minZ, maxX, maxZ,
				LayoutUtil.BASE_MATERIAL);
	}

	protected void horizontalRect(byte[][] result, int y, int minX, int minZ,
			int maxX, int maxZ, Material mat) {
		for (int i = minX; i <= maxX; i++) {
			LayoutUtil.setBlock(result, i, y, minZ, mat);
			LayoutUtil.setBlock(result, i, y, maxZ, mat);
		}

		for (int i = minZ; i <= maxZ; i++) {
			LayoutUtil.setBlock(result, minX, y, i, mat);
			LayoutUtil.setBlock(result, maxX, y, i, mat);
		}
	}

	protected void drawIfFill(byte[][] result, int bottomY, int x, int z,
			int minY, int maxY, int rangeMin, int rangeMax) {
		for (int y = minY; y < maxY; y++) {
			if (x >= rangeMin && x <= rangeMax) {
				if (z >= rangeMin && z <= rangeMax)
					LayoutUtil.setBlock(result, x, bottomY + y, z,
							LayoutUtil.BASE_MATERIAL);

			}
		}
	}

	protected void placeTorch(Chunk chunk, int x, int z, int y, int... locs) {
		for (int l : locs) {
			Block xs = chunk.getBlock(x, y, l);
			xs.setType(Material.TORCH);
			setBlockDirection(xs);

			Block zs = chunk.getBlock(l, y, z);
			zs.setType(Material.TORCH);
			setBlockDirection(zs);
		}
	}

	private void setBlockDirection(Block b) {
		if (b.getRelative(BlockFace.NORTH).getType() != Material.AIR)
			b.setData(LayoutUtil.TORCH_POINTING_SOUTH);

		if (b.getRelative(BlockFace.EAST).getType() != Material.AIR)
			b.setData(LayoutUtil.TORCH_POINTING_WEST);

		if (b.getRelative(BlockFace.SOUTH).getType() != Material.AIR)
			b.setData(LayoutUtil.TORCH_POINTING_NORTH);

		if (b.getRelative(BlockFace.WEST).getType() != Material.AIR)
			b.setData(LayoutUtil.TORCH_POINTING_EAST);

	}

	protected void applyTunnelEntrances(byte[][] result, int bottomY,
			int chunkX, int chunkZ) {
		if (LayoutUtil.isTunnel(chunkX - 1, chunkZ))
			LayoutUtil.applyGate(result, bottomY, 0, ShapeMode.X);
		if (LayoutUtil.isTunnel(chunkX + 1, chunkZ))
			LayoutUtil.applyGate(result, bottomY, 15, ShapeMode.X);
		if (LayoutUtil.isTunnel(chunkX, chunkZ - 1))
			LayoutUtil.applyGate(result, bottomY, 0, ShapeMode.Z);
		if (LayoutUtil.isTunnel(chunkX, chunkZ + 1))
			LayoutUtil.applyGate(result, bottomY, 15, ShapeMode.Z);
	}

	protected void doorAt(Chunk chunk, int x, int bottomY, int z,
			Material type, BlockFace facingDirection) {
		Block lower = chunk.getBlock(x, bottomY, z);
		Block upper = chunk.getBlock(x, bottomY + 1, z);

		Door ld = new Door(type);
		ld.setOpen(false);
		ld.setFacingDirection(facingDirection);
		ld.setTopHalf(false);

		Door ud = new Door(type);
		ud.setOpen(false);
		ud.setFacingDirection(facingDirection);
		ud.setTopHalf(true);

		lower.setType(ld.getItemType());
		lower.setData(ld.getData());

		upper.setType(ud.getItemType());
		upper.setData(ud.getData());

	}

	protected void renderBottom(byte[][] result, int bottomY, long seed,
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

	protected void torchAt(Chunk chunk, int x, int y, int z,
			BlockFace facingDirection) {
		Block block = chunk.getBlock(x, y, z);

		Torch torch = new Torch();
		torch.setFacingDirection(facingDirection);

		block.setType(Material.TORCH);
		block.setData(torch.getData());

	}

	protected void ladderAt(Chunk chunk, int x, int y, int z,
			BlockFace facingDirection) {
		Block block = chunk.getBlock(x, y, z);

		Ladder ladder = new Ladder();
		ladder.setFacingDirection(facingDirection);

		block.setType(Material.LADDER);
		block.setData(ladder.getData());

	}
}
