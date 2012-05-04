package com.jeroensteenbeeke.bk.cityofthegods;

import java.util.Random;

import com.jeroensteenbeeke.bk.cityofthegods.buildings.Fountain;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.FourHouse;
import com.jeroensteenbeeke.bk.cityofthegods.buildings.Spire;

final class Buildings {
	private static final Building[] buildings = new Building[] {
			new Fountain(), new Spire(), new FourHouse() };

	static Building getRandom(long seed, int chunkX, int chunkZ) {

		Random random = new Random(seed);

		final int base = -2 * LayoutUtil.MAX_BOUND;

		for (int i = base; i < chunkX; i++) {
			for (int j = base; j < (256 * chunkZ); j++) {
				random.nextBoolean();
			}
		}

		return buildings[random.nextInt(buildings.length)];
	}
}
