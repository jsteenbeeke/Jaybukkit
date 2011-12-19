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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;

public class WaypointCreateHandler extends PlayerAwareCommandHandler {
	private final WaypointPlugin plugin;

	public WaypointCreateHandler(WaypointPlugin plugin) {
		super(plugin.getServer(), WaypointPlugin.CREATE_PERMISSION);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("wp-create").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String name = args[0];

		if (plugin.getWaypoints().getWaypoint(name) == null) {
			int lx = player.getLocation().getBlockX();
			int y = player.getLocation().getBlockY();
			int lz = player.getLocation().getBlockZ();

			Messages.send(player, "Scanning location (&e" + lx + "&f,&e" + y
					+ "&f,&e" + lz + "&f) for waypoint suitability");

			if (y > player.getWorld().getMaxHeight() - 2) {
				Messages.send(player, "&Location too high for waypoint");
				return;
			}

			int i = -2;

			for (int x = lx - 2; x <= lx + 2; x++) {
				int j = -2;

				for (int z = lz - 2; z <= lz + 2; z++) {
					if (Math.abs(i) != 2 || Math.abs(j) != 2) {
						if (plugin.getWaypoints().isWaypoint(x, y, z)) {
							Messages.send(player,
									"&cUnsuitable location, already contains waypoint");
							return;
						}

						Block mustBeAir = player.getWorld().getBlockAt(x, y, z);
						Block mustBeSolid = player.getWorld().getBlockAt(x,
								y - 1, z);

						if (!mustBeAir.isEmpty()
								&& mustBeAir.getType() != Material.SNOW) {
							Messages.send(player,
									"&cUnsuitable location, area not clear");
							Messages.send(player, "&cLocation (&e" + x
									+ "&c,&e" + y + "&c,&e" + z + "&c) is "
									+ mustBeAir.getType().toString());
							return;
						}

						if (mustBeSolid.isEmpty() || mustBeSolid.isLiquid()) {
							Messages.send(player,
									"&cUnsuitable location, area not on top of solid blocks");
							if (mustBeSolid.isEmpty()) {

								Messages.send(player, "&cLocation (&e" + x
										+ "&c,&e" + y + "&c,&e" + z
										+ "&c) is air");
							} else if (mustBeSolid.isLiquid()) {
								Messages.send(player, "&cLocation (&e" + x
										+ "&c,&e" + y + "&c,&e" + z
										+ "&c) is liquid");
							}
							return;
						}

					}

					j++;
				}

				i++;
			}

			// Location suitable

			i = -2;

			for (int x = lx - 2; x <= lx + 2; x++) {
				int j = -2;

				for (int z = lz - 2; z <= lz + 2; z++) {
					if (Math.abs(i) != 2 || Math.abs(j) != 2) {
						Block b = player.getWorld().getBlockAt(x, y, z);
						if (Math.abs(i) == 2 || Math.abs(j) == 2) {
							// Outer layer, single slab
							b.setTypeId(44);
						} else if (Math.abs(i) == 1 || Math.abs(j) == 1) {
							// Inner layer, double slab
							b.setTypeId(43);
						} else {
							// Center, lightstone
							b.setType(Material.GLOWSTONE);
						}

					}

					j++;
				}

				i++;
			}

			Location loc = new Location(player.getWorld(), lx, y, lz);

			plugin.getWaypoints().newWaypoint(loc, name);
			player.teleport(loc.add(0, 2, 0));

			Messages.send(player,
					String.format("Waypoint &a%s&f created", name));
		} else {
			Messages.send(player,
					String.format("&cWaypoint &a%s&c already exists", name));
		}

	}
}
