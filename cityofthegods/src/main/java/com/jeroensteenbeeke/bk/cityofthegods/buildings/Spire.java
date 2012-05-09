package com.jeroensteenbeeke.bk.cityofthegods.buildings;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import com.jeroensteenbeeke.bk.cityofthegods.Building;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil;
import com.jeroensteenbeeke.bk.cityofthegods.LayoutUtil.ShapeMode;

//WEST
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 15
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 14
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 13
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 12
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|SS|SS|SS|SS|SS|SS|--|--|--|--|--| 11
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|SS|SS|SS|SS|SS|SS|SS|SS|--|--|--|--| 10
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|SS|SS|FF|FF|FF|FF|SS|SS|--|--|--|--| 9
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|SS|SS|FF|--|--|FF|SS|SS|--|--|--|--| 8
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ SOUTH
//|--|--|--|--|SS|SS|FF|--|--|FF|SS|SS|--|--|--|--| 7
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|SS|SS|FF|FF|FF|FF|SS|SS|--|--|--|--| 6
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|SS|SS|SS|GG|GG|SS|SS|SS|--|--|--|--| 5
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|SS|SS|GG|GG|SS|SS|--|--|--|--|--| 4
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 3
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 2
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 1
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--| 0
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//|00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15| ^--- Z
//+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//-----X--->
public class Spire extends Building {
	@Override
	public void generateLevel(byte[][] result, int bottomY, long seed,
			int chunkX, int chunkZ) {
		if (bottomY >= 48 && bottomY <= 128) {
			final int base = (bottomY == 48) ? 51 : bottomY;

			for (int y = bottomY; y < bottomY + 16; y++) {
				if (y >= base) {
					horizontalRect(result, y, 4, 5, 11, 10);
					horizontalRect(result, y, 5, 4, 10, 11);
				}
				int mod = y % 4;

				int stairsX = 7;
				int stairsZ = 8;

				switch (mod) {
				case 0:
					horizontalRect(result, y, 6, 6, 9, 9);
					break;
				case 2:
					if (y >= base) {
						horizontalRect(result, y, 7, 4, 8, 5, Material.GLASS);
						horizontalRect(result, y, 4, 7, 5, 8, Material.GLASS);
						horizontalRect(result, y, 10, 7, 11, 8, Material.GLASS);
						horizontalRect(result, y, 7, 10, 8, 11, Material.GLASS);
					}
					break;
				}

				LayoutUtil.setBlock(result, stairsX, y, stairsZ,
						LayoutUtil.BASE_MATERIAL);
			}
		}

		if (bottomY == 48) {
			horizontalRect(result, 48, 7, 7, 8, 8);
			horizontalRect(result, 48, 6, 6, 9, 9);
			horizontalRect(result, 48, 5, 5, 10, 10);
			horizontalRect(result, 48, 4, 4, 11, 11, Material.GLOWSTONE);
		}

		if (bottomY == 128) {
			horizontalRect(result, 143, 6, 6, 9, 9);
			horizontalRect(result, 143, 5, 5, 10, 10);
			horizontalRect(result, 143, 4, 4, 11, 11);
			horizontalRect(result, 143, 3, 3, 12, 12, Material.GLOWSTONE);
		}

		if (bottomY == 64) {
			if (LayoutUtil.isTunnel(chunkX - 1, chunkZ)) {
				for (int i = 0; i < 4; i++)
					LayoutUtil.applyTunnel(result, bottomY, i, ShapeMode.X);
				LayoutUtil.applyGate(result, bottomY, 4, ShapeMode.X);
				LayoutUtil.applyGate(result, bottomY, 5, ShapeMode.X);
			}
			if (LayoutUtil.isTunnel(chunkX + 1, chunkZ)) {
				for (int i = 0; i < 4; i++)
					LayoutUtil
							.applyTunnel(result, bottomY, 15 - i, ShapeMode.X);
				LayoutUtil.applyGate(result, bottomY, 11, ShapeMode.X);
				LayoutUtil.applyGate(result, bottomY, 10, ShapeMode.X);
			}
			if (LayoutUtil.isTunnel(chunkX, chunkZ - 1)) {
				for (int i = 0; i < 4; i++)
					LayoutUtil.applyTunnel(result, bottomY, i, ShapeMode.Z);
				LayoutUtil.applyGate(result, bottomY, 4, ShapeMode.Z);
				LayoutUtil.applyGate(result, bottomY, 5, ShapeMode.Z);
			}
			if (LayoutUtil.isTunnel(chunkX, chunkZ + 1)) {
				for (int i = 0; i < 4; i++)
					LayoutUtil
							.applyTunnel(result, bottomY, 15 - i, ShapeMode.Z);
				LayoutUtil.applyGate(result, bottomY, 11, ShapeMode.Z);
				LayoutUtil.applyGate(result, bottomY, 10, ShapeMode.Z);
			}
		}

	}

	@Override
	public void onPopulate(Chunk chunk) {
		for (int y = 51; y < 144; y++) {
			if (y % 4 == 2) {
				torchAt(chunk, 6, y, 6, BlockFace.WEST);
				torchAt(chunk, 9, y, 6, BlockFace.WEST);

				torchAt(chunk, 6, y, 9, BlockFace.EAST);
				torchAt(chunk, 9, y, 9, BlockFace.EAST);
			}
		}

		for (int y = 49; y < 144; y++) {
			ladderAt(chunk, 8, y, 8, BlockFace.NORTH);
		}
	}

}
