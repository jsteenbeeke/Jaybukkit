package com.jeroensteenbeeke.bk.dropteleport;

import java.util.Map;

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
		int y = target.getMaxHeight();

		Block b = target.getBlockAt(x, y, z);

		if (target.getEnvironment() == Environment.NETHER) {

			while (!b.isEmpty() && y > 0) {
				b = target.getBlockAt(x, --y, z);
			}
		}

		while (b.isEmpty() && y > 0) {
			b = target.getBlockAt(x, --y, z);
		}

		return y;
	}
}
