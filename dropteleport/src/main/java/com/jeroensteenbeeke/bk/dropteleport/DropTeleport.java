package com.jeroensteenbeeke.bk.dropteleport;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class DropTeleport extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled dropteleport plugin");

		Map<String, DropRoute> worlds = Maps.newHashMap();
		for (World world : getServer().getWorlds()) {
			final String worldname = world.getName();

			if (getConfig().contains(worldname)) {
				logger.info(String.format(
						"Found DropTeleport section for world %s", worldname));
				ConfigurationSection section = getConfig()
						.getConfigurationSection(worldname);

				boolean hasTopHeight = section.contains("height");
				boolean hasTargetWorld = section.contains("targetWorld");

				if (!hasTopHeight) {
					logger.severe("Missing property 'height'");
				}
				if (!hasTargetWorld) {
					logger.severe("Missing property 'targetWorld");
				}

				if (hasTopHeight && hasTargetWorld) {
					final int topHeight = section.getInt("height", 20);
					final String targetWorldName = section
							.getString("targetWorld");
					final World targetWorld = getServer().getWorld(
							targetWorldName);

					if (targetWorld != null) {
						DropRoute route = new DropRoute(targetWorld, topHeight);
						worlds.put(worldname, route);
					} else {
						logger.severe(String.format(
								"Invalid target world name: '%s'",
								targetWorldName));
					}

				}
			}
		}

		addListener(new DropListener(worlds));
	}

	@Override
	public void onDisable() {

	}
}
