package com.jeroensteenbeeke.bk.waypoint.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.jeroensteenbeeke.bk.waypoint.Waypoints;

public class BlockProtectionListener extends BlockListener {
	private final Waypoints waypoints;

	public BlockProtectionListener(Waypoints waypoints) {
		super();
		this.waypoints = waypoints;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		int x = event.getBlock().getX();
		int y = event.getBlock().getY();
		int z = event.getBlock().getZ();

		if (waypoints.isWaypoint(x, y, z)) {
			event.setCancelled(true);
			event.getPlayer()
					.sendMessage("\u00A7cCannot edit waypoints\u00A7f");
		}
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.isCancelled())
			return;

		int x = event.getBlock().getX();
		int y = event.getBlock().getY();
		int z = event.getBlock().getZ();

		if (waypoints.isWaypoint(x, y, z)) {
			event.setCancelled(true);
			event.getPlayer()
					.sendMessage("\u00A7cCannot edit waypoints\u00A7f");
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		int x = event.getBlock().getX();
		int yb = event.getBlock().getY();
		int z = event.getBlock().getZ();

		for (int y = yb; y > yb - 4; y--) {
			if (waypoints.isWaypoint(x, y, z)) {
				event.setCancelled(true);
				event.getPlayer()
						.sendMessage(
								"\u00A7cCannot place blocks within Waypoint space\u00A7f");
			}
		}
	}
}
