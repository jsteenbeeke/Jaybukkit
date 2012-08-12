package com.jeroensteenbeeke.bk.endblockade;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class EndTeleportListener implements Listener {
	private final Server server;

	public EndTeleportListener(Server server) {
		super();
		this.server = server;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTravelToEnd(PlayerPortalEvent portalEvent) {
		if (portalEvent.isCancelled())
			return;

		Location to = portalEvent.getTo();
		if (to.getWorld().getEnvironment() == Environment.THE_END) {
			portalEvent.setCancelled(true);

			// Try to save the player from Death by Lava
			Player player = portalEvent.getPlayer();
			Location spawn = player.getBedSpawnLocation();
			if (spawn == null) {
				for (World world : server.getWorlds()) {
					if (world.getEnvironment() == Environment.NORMAL) {
						spawn = world.getSpawnLocation();
						break;
					}
				}
			}

			if (spawn != null) {
				player.teleport(spawn);
			}
		}
	}
}
