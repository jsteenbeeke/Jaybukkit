package com.jeroensteenbeeke.bk.timefreeze;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.World;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class TimeFreeze extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled timefreeze plugin");

		Map<UUID, Long> times = Maps.newHashMap();

		for (World world : getServer().getWorlds()) {
			long time = getConfig().getLong(world.getName(), Long.MIN_VALUE);

			if (time != Long.MIN_VALUE && time >= 0L) {
				logger.info(String.format("Will freeze time of %s at %d",
						world.getName(), time));

				UUID uuid = world.getUID();
				times.put(uuid, time);
			}
		}

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new FreezeTask(getServer(), times), 20L, 600L);
	}

	@Override
	public void onDisable() {

	}
}
