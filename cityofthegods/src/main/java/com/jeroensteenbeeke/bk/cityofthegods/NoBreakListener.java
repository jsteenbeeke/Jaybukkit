package com.jeroensteenbeeke.bk.cityofthegods;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class NoBreakListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreakBlock(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		if (event.getBlock().getWorld().getGenerator() instanceof CityOfTheGodsGenerator) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlaceBlock(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		if (event.getBlock().getWorld().getGenerator() instanceof CityOfTheGodsGenerator) {
			event.setCancelled(true);
		} else {

		}

	}
}
