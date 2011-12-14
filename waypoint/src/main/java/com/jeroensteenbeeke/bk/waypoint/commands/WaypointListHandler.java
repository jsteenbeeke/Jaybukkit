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
package com.jeroensteenbeeke.bk.waypoint.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class WaypointListHandler extends PlayerAwareCommandHandler {
	private final WaypointPlugin plugin;

	public WaypointListHandler(WaypointPlugin plugin) {
		super(plugin.getServer(), WaypointPlugin.USE_PERMISSION);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "wp-list".equals(command.getName()) && args.length == 0;
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			List<Waypoint> waypoints = plugin.getWaypoints()
					.getWaypointsByWorld(player.getWorld().getName());

			StringBuilder msg = new StringBuilder();
			msg.append("Waypoints in your world (&e" + waypoints.size()
					+ "&f): &a");

			int i = 0;
			for (Waypoint waypoint : waypoints) {
				if (i++ > 0) {
					msg.append("&f, &a");
				}

				msg.append(waypoint.getName());
			}

			Messages.send(player, msg.toString());

			return true;
		}
		return false;
	}

}
