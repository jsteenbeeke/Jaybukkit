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

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class WaypointDestroyHandler extends PlayerAwareCommandHandler {
	private final WaypointPlugin plugin;

	public WaypointDestroyHandler(WaypointPlugin plugin) {
		super(plugin.getServer(), WaypointPlugin.CREATE_PERMISSION);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("wp-destroy").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

		String name = args[0];

		Waypoint wp = plugin.getWaypoints().getWaypoint(name);

		if (wp != null) {
			plugin.getWaypoints().destroy(wp);

			int lx = wp.getX();
			int y = wp.getY();
			int lz = wp.getZ();

			int i = -2;

			for (int x = lx - 2; x <= lx + 2; x++) {
				int j = -2;

				for (int z = lz - 2; z <= lz + 2; z++) {
					if (Math.abs(i) != 2 || Math.abs(j) != 2) {
						plugin.getServer().getWorld(wp.getWorldName())
								.getBlockAt(x, y, z).setType(Material.AIR);
					}
					j++;
				}
				i++;
			}

			plugin.getDatabase().delete(wp);

			Messages.broadcast(plugin.getServer(),
					String.format("&cWaypoint &e%s &cdestroyed", name));
		} else {
			Messages.send(player,
					String.format("&cUnknown waypoint &e%s", name));
		}

		return true;
	}
}
