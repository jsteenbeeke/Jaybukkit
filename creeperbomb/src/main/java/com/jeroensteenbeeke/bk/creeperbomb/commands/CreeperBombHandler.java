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
package com.jeroensteenbeeke.bk.creeperbomb.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.creeperbomb.Plugin;

public class CreeperBombHandler implements CommandHandler {
	private final JSPlugin plugin;

	public CreeperBombHandler(JSPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "creeperbomb".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (sender.hasPermission(Plugin.PERMISSION_NAME)) {
			if (args.length == 2) {
				Player player = plugin.getServer().getPlayerExact(args[0]);

				if (player != null) {
					try {
						int radius = Integer.parseInt(args[1]);

						if (radius >= 2 && radius <= 5) {

							Location loc = player.getLocation();

							List<Location> locs = getRadius(loc, radius);

							int success = 0;

							for (Location next : locs) {
								if (player.getWorld().spawnCreature(next,
										CreatureType.CREEPER) != null)
									success++;
							}

							if (success > 2) {
								plugin.getServer().broadcastMessage(
										"LET THERE BE CREEPERS!");
							}

							return true;
						} else {
							sender.sendMessage("Radius must be at least 2 and at most 5");
						}
					} catch (NumberFormatException nfe) {
						sender.sendMessage("Invalid radius: " + args[1] + " - "
								+ nfe.getMessage());
					}

				} else {
					sender.sendMessage("Unknown player");
				}
			} else {
				sender.sendMessage("No player and/or radius specified");
			}
		}

		else {
			sender.sendMessage("You do not have permission to use creeperbombs");
		}

		return false;
	}

	private List<Location> getRadius(Location loc, int radius) {
		List<Location> result = new ArrayList<Location>(50);

		int x = loc.getBlockX();
		int z = loc.getBlockZ();

		for (int xi = (x - radius); xi <= (x + radius); xi++) {
			for (int zi = (z - radius); zi <= (z + radius); zi++) {
				int adx = Math.abs(xi - x);
				int adz = Math.abs(zi - z);
				long dist = Math.round(Math.sqrt(adx * adx + adz * adz));

				if (dist <= radius) {
					result.add(new Location(loc.getWorld(), xi, loc.getY(), zi));
				}
			}
		}

		return result;
	}

}
