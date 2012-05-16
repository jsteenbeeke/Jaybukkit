package com.jeroensteenbeeke.bk.travelagent;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.World;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class TravelAgent extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled travelagent plugin");

		Map<String, World> netherMappings = Maps.newHashMap();

	}

	@Override
	public void onDisable() {

	}
}
