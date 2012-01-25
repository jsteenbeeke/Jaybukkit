package com.jeroensteenbeeke.bk.labgen;

import java.util.logging.Logger;

import org.bukkit.generator.ChunkGenerator;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class LabGen extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled labgen plugin");

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new NoonTask(this), 20L, 600L);
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {

		return new LabGenerator();
	}

	@Override
	public void onDisable() {

	}
}
