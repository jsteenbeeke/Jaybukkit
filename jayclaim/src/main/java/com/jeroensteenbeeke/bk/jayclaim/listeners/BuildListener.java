package com.jeroensteenbeeke.bk.jayclaim.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayclaim.JayClaim;
import com.jeroensteenbeeke.bk.jayclaim.entities.Claim;

public class BuildListener implements Listener {
	private final JayClaim jayclaim;

	public BuildListener(JayClaim jayclaim) {
		this.jayclaim = jayclaim;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.isCancelled())
			return;

		if (!mayChange(event.getPlayer(), event.getBlock().getLocation())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou do not have build rights in this area");
		}

	}

	private boolean mayChange(Player player, Location location) {
		final String playerName = player.getName();

		final Claim claim = jayclaim.getTracker().getClaimAt(location);

		if (claim != null) {

		}

		return false;
	}

}
