package com.jeroensteenbeeke.bk.clanblah;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.block.Block;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.clanblah.commands.TeleportToBattleShipCommand;

public class ClanBlah extends JSPlugin {
	public static final String PERMISSION_BLAH_MEMBER = "blah.member";

	private static final String CONFIG_WORLD_KEY = "battleship_world";

	private static final String CONFIG_LOCATION_KEY = "battleship_location";

	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("All Hail Clan Blah!");

		addCommandHandler(new TeleportToBattleShipCommand(this));
	}

	@Override
	public void onDisable() {

	}

	public Block getBattleShipLocation() {
		String worldName = getConfig().getString(CONFIG_WORLD_KEY);
		if (worldName != null) {
			World world = getServer().getWorld(worldName);

			if (world != null) {
				List<Integer> coords = getConfig().getIntegerList(
						CONFIG_LOCATION_KEY);
				if (coords != null && coords.size() == 3) {
					int x = coords.get(0);
					int y = coords.get(1);
					int z = coords.get(2);

					return world.getBlockAt(x, y, z);
				}
			}

		}
		return null;

	}
}
