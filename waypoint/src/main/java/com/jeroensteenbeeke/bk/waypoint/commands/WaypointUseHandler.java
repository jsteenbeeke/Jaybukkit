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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class WaypointUseHandler implements CommandHandler
{
	private final WaypointPlugin plugin;

	public WaypointUseHandler(WaypointPlugin plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args)
	{
		return "wp-go".equals(command.getName()) && args.length == 1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 1)
		{
			if (sender.hasPermission(WaypointPlugin.USE_PERMISSION))
			{
				Player player = plugin.getServer().getPlayerExact(sender.getName());
				Waypoint wp = plugin.getWaypoints().getWaypoint(player.getLocation());

				if (wp != null)
				{
					String target = args[0];
					if (!target.equals(wp.getName()))
					{
						Waypoint targetWaypoint = plugin.getWaypoints().getWaypoint(target);
						if (targetWaypoint != null)
						{
							World targetWorld =
								plugin.getServer().getWorld(targetWaypoint.getWorldName());
							if (targetWorld.equals(player.getWorld()))
							{
								Location location =
									new Location(targetWorld, targetWaypoint.getX(),
										targetWaypoint.getY() + 1, targetWaypoint.getZ());

								player.teleport(location);
								player.sendMessage("Teleporting to \u00A7a" + targetWaypoint.getName()
									+ "\u00A7f");
								return true;
							}
							else
							{
								player.sendMessage("\u00A7cCannot use waypoints to cross dimensions\u00A7f");
								return true;
							}

						}
						else
						{
							player.sendMessage("\u00A7cUnknown waypoint: \u00A7a" + target + "\u00A7f");
							return true;
						}
					}
					else
					{
						player.sendMessage("\u00A7cYou are already there\u00A7f");
						return true;
					}
				}
				else
				{
					player.sendMessage("\u00A7cThis command can only be used on Waypoints\u00A7f");
					return true;
				}
			}
			else
			{
				sender.sendMessage("\u00A7cYou do not have permission to use Waypoints\u00A7f");
				return true;
			}
		}

		return false;
	}
}
