package com.jeroensteenbeeke.bk.bedsploder;

import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.World;

import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class BedSploder extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled bedsploder plugin");

		Set<String> sploderWorlds = Sets.newHashSet();

		for (World world : getServer().getWorlds()) {
			boolean hasSploder = getConfig().getBoolean(world.getName(), false);

			if (hasSploder) {
				sploderWorlds.add(world.getName());
			}
		}

		if (!sploderWorlds.isEmpty()) {
			addListener(new BedListener(sploderWorlds));
		}

	}

	@Override
	public void onDisable() {

	}
}
