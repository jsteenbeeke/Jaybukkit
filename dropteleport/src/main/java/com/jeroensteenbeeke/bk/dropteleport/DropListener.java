package com.jeroensteenbeeke.bk.dropteleport;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DropListener implements Listener {
	private Logger logger = Logger.getLogger("Minecraft");

	private final Map<String, DropRoute> worlds;

	public DropListener(Map<String, DropRoute> worlds) {
		super();
		this.worlds = worlds;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDrop(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		final Location from = event.getFrom();
		final String fromWorldName = from.getWorld().getName();

		if (worlds.containsKey(fromWorldName)) {
			DropRoute route = worlds.get(fromWorldName);

			if (from.getY() <= route.getMaxHeight()) {
				int x = from.getBlockX();
				int z = from.getBlockZ();

				World target = route.getTargetWorld();
				int y = getTargetWorldLocation(target, x, z);

				Location targetLocation = new Location(route.getTargetWorld(),
						x, y, z);

				player.teleport(targetLocation);
			}
		}
	}

	private int getTargetWorldLocation(World target, int x, int z) {
		int y = target.getMaxHeight() - 1;

		logger.info(String.format("Max height is %d", y));

		if (y == 0)
			y = 255;

		Block b = target.getBlockAt(x, y, z);

		if (target.getEnvironment() == Environment.NETHER) {
			int i = 0;
			while (!b.isEmpty() && y > 0) {
				b = target.getBlockAt(x, --y, z);
				i++;
			}
			logger.info(String.format("Nether algorithm lowered %d times", i));
		}

		int i = 0;

		logger.info(String.format("Block is %s", b.getType()));

		while (b.isEmpty() && y > 0) {
			b = target.getBlockAt(x, --y, z);
			i++;
			logger.info(String.format("Block is %s", b.getType()));
		}

		logger.info(String.format("Regular algorithm lowered %d times", i));

		return y;
	}
}
