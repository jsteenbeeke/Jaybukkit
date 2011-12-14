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
package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class WeatherCommandHandler implements CommandHandler {
	@Override
	public boolean matches(Command command, String[] args) {
		return "weather".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			if (sender.hasPermission(JayOp.PERMISSION_ENVIRONMENT)) {
				Player player = sender.getServer().getPlayerExact(
						sender.getName());

				if ("sun".equals(args[0])) {
					player.getWorld().setStorm(false);
					player.getWorld().setThundering(false);

					sender.getServer().broadcastMessage(
							"Weather changed to \u00A7esun\u00A7f");

					return true;
				}
				if ("rain".equals(args[0]) || "snow".equals(args[0])) {
					player.getWorld().setStorm(true);
					player.getWorld().setThundering(false);

					sender.getServer().broadcastMessage(
							"Weather changed to \u00A7erain and snow\u00A7f");

					return true;
				}
				if ("thunderstorm".equals(args[0])) {
					sender.getServer().broadcastMessage(
							"Weather changed to \u00A7ethunderstorm\u00A7f");

					player.getWorld().setStorm(true);
					player.getWorld().setThundering(true);

					return true;
				}

				return false;
			} else {
				sender.sendMessage("\u00A7cYou do not have permission to change the weather\u00A7f");
				return true;
			}
		}

		return false;
	}
}
