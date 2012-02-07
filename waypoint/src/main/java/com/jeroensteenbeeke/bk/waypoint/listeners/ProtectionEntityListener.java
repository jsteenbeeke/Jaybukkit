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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.Waypoints;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class ProtectionEntityListener implements Listener {
	private final Waypoints waypoints;

	private WaypointPlugin plugin;

	public ProtectionEntityListener(WaypointPlugin plugin, Waypoints waypoints) {
		this.waypoints = waypoints;
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;

		for (Block block : event.blockList()) {
			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();

			if (waypoints.isWaypoint(x, y, z)) {
				Entity entity = event.getEntity();
				World world = entity.getWorld();

				Waypoint waypoint = waypoints.getWaypoint(new Location(world,
						x, y, z));

				event.setCancelled(true);
				plugin.getServer().broadcast(
						"Explosion prevented near waypoint \u00A7a"
								+ waypoint.getName()
								+ "\u00A7f in world \u00A7e" + world.getName()
								+ "\u00A7f", WaypointPlugin.CREATE_PERMISSION);
				return;
			}
		}
	}

}
