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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class WaypointUseHandler extends PlayerAwareCommandHandler {
	private final WaypointPlugin plugin;

	public WaypointUseHandler(WaypointPlugin plugin) {
		super(plugin.getServer(), WaypointPlugin.USE_PERMISSION);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("wp-go").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

		if (args.length == 1) {
			Waypoint wp = plugin.getWaypoints().getWaypoint(
					player.getLocation());

			if (wp != null) {
				String target = args[0];
				if (!target.equals(wp.getName())) {
					Waypoint targetWaypoint = plugin.getWaypoints()
							.getWaypoint(target);
					if (targetWaypoint != null) {
						World targetWorld = plugin.getServer().getWorld(
								targetWaypoint.getWorldName());
						if (targetWorld.equals(player.getWorld())) {
							Location location = new Location(targetWorld,
									targetWaypoint.getX(),
									targetWaypoint.getY() + 1,
									targetWaypoint.getZ());

							player.teleport(location);
							Messages.send(player, "Teleporting to &a"
									+ targetWaypoint.getName() + "");
							return true;
						} else {
							Messages.send(player,
									"&cCannot use waypoints to cross dimensions");
							return true;
						}

					} else {
						Messages.send(player, "&cUnknown waypoint: &a" + target
								+ "");
						return true;
					}
				} else {
					Messages.send(player, "&cYou are already there");
					return true;
				}
			} else {
				Messages.send(player,
						"&cThis command can only be used on Waypoints");
				return true;
			}

		}

		return false;
	}
}
