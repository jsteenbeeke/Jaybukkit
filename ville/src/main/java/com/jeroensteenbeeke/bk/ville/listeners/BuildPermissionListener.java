package com.jeroensteenbeeke.bk.ville.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.VilleLocations;

public class BuildPermissionListener implements Listener {
	private final VilleLocations locations;

	public BuildPermissionListener(Ville ville) {
		locations = ville.getLocations();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event.getBlock()
				.getLocation())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou do not have permission to build here");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event.getBlock()
				.getLocation())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou do not have permission to build here");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event.getBlock()
				.getLocation())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou do not have permission to build here");
		}
	}

}
