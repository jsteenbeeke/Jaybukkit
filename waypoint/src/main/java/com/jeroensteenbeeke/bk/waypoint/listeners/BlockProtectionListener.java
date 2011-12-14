/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
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
