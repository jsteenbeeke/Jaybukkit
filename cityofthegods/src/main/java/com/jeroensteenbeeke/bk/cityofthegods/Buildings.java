package com.jeroensteenbeeke.bk.cityofthegods;

import java.util.Random;

import com.jeroensteenbeeke.bk.cityofthegods.buildings.FlowerPatch;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.Forum;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.Fountain;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.FourHouse;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.Mansion;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.Pyramid;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.Spire;

final class Buildings {
	private static final Building[] buildings = new Building[] {
			new Fountain(), new Pyramid(), new FourHouse(), new Mansion(),
			new Forum(), new Spire(), new FlowerPatch() };

	static Building getRandom(long seed, int chunkX, int chunkZ) {

		Random random = new Random(seed);

		final int base = -2 * LayoutUtil.MAX_BOUND;

		for (int i = base; i < chunkX; i++) {
			for (int j = base * 256; j < chunkZ; j++) {
				random.nextBoolean();
			}
		}

		return buildings[random.nextInt(buildings.length)];
	}
}
