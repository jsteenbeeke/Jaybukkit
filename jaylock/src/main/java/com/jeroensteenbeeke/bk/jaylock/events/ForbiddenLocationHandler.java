package com.jeroensteenbeeke.bk.jaylock.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.jeroensteenbeeke.bk.jaylock.Jaylock;
import com.jeroensteenbeeke.bk.jaylock.entities.ForbiddenLocation;

public class ForbiddenLocationHandler implements Listener {
	private final Jaylock jaylock;

	public ForbiddenLocationHandler(Jaylock jaylock) {
		super();
		this.jaylock = jaylock;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent moveEvent) {
		Location from = moveEvent.getFrom();
		Location to = moveEvent.getTo();

		if (!from.getBlock().equals(to.getBlock())) {
			if (!moveAllowed(moveEvent.getTo(), moveEvent.getPlayer())) {
				moveEvent.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTeleport(PlayerTeleportEvent teleportEvent) {
		if (!moveAllowed(teleportEvent.getTo(), teleportEvent.getPlayer())) {
			teleportEvent.setCancelled(true);
		}
	}

	private boolean moveAllowed(Location to, Player player) {
		if (to != null && player != null) {
			Block b = to.getBlock();
			if (b != null) {
				ForbiddenLocation loc = jaylock.getForbiddenLocation(b);
				if (loc == null)
					return true;

				if (loc.getOwner() == null
						&& player
								.hasPermission(Jaylock.PERMISSION_JAYLOCK_ADMIN))
					return true;

				if (loc.getOwner().toLowerCase()
						.equals(player.getName().toLowerCase()))
					return true;
			}
		}

		return false;
	}

}
