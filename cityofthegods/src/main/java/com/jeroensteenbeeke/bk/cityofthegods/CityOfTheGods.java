package com.jeroensteenbeeke.bk.cityofthegods;

import java.util.logging.Logger;

import org.bukkit.generator.ChunkGenerator;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class CityOfTheGods extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled cityofthegods plugin");

	}

	@Override
	public void onDisable() {

	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new CityOfTheGodsGenerator();
	}
}
