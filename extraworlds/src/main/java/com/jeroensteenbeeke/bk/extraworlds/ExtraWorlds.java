package com.jeroensteenbeeke.bk.extraworlds;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class ExtraWorlds extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled extraworlds plugin");

		if (getConfig().contains("extraworlds")) {
			final List<String> extraWorlds = getConfig().getStringList(
					"extraworlds");

			for (String world : extraWorlds) {
				logger.info(String.format("Creating extra world %s", world));

				getServer()
						.createWorld(
								new WorldCreator(world)
										.environment(Environment.NORMAL));
			}
		}
	}

	@Override
	public void onDisable() {

	}
}
