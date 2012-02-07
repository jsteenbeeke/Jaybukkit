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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.jeroensteenbeeke.bk.waypoint.Waypoints;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class MovementListener implements Listener {
	private Waypoints waypoints;

	public MovementListener(Waypoints waypoints) {
		this.waypoints = waypoints;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Waypoint to = waypoints.getWaypoint(event.getTo());
		Waypoint from = waypoints.getWaypoint(event.getFrom());

		if (to != null && from == null) {
			event.getPlayer().sendMessage(
					"Entered waypoint \u00A7a" + to.getName() + "\u00A7f");
		} else if (to == null && from != null) {
			event.getPlayer().sendMessage(
					"Exited waypoint \u00A7a" + from.getName() + "\u00A7f");
		}
	}
}
