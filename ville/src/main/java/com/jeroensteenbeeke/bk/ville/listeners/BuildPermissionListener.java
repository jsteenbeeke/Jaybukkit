package com.jeroensteenbeeke.bk.ville.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.VilleLocations;

public class BuildPermissionListener extends BlockListener {
	private final VilleLocations locations;

	public BuildPermissionListener(Ville ville) {
		locations = ville.getLocations();
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event.getBlock()
				.getLocation()))
			event.setCancelled(true);
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event.getBlock()
				.getLocation()))
			event.setCancelled(true);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event.getBlock()
				.getLocation()))
			event.setCancelled(true);
	}

}
