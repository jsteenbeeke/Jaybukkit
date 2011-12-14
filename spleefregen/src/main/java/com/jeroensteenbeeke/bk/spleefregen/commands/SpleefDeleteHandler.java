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
package com.jeroensteenbeeke.bk.spleefregen.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefDeleteHandler implements CommandHandler {

	private final SpleefRegen plugin;

	public SpleefDeleteHandler(SpleefRegen plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "unspleef".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(SpleefRegen.PERMISSION_SPLEEF_REMOVE)) {
			if (args.length == 1) {
				Player player = plugin.getServer().getPlayerExact(
						sender.getName());
				if (player != null) {
					String name = args[0];

					SpleefPoint point = plugin.getDatabase()
							.createQuery(SpleefPoint.class).where()
							.eq("name", name).findUnique();

					if (point != null) {
						World world = plugin.getServer().getWorld(
								point.getWorld());

						for (SpleefLocation loc : point.getLocations()) {
							Block b = world.getBlockAt(loc.getX(), loc.getY(),
									loc.getZ());
							b.setType(Material.AIR);

							plugin.getDatabase().delete(loc);
						}

						plugin.getDatabase().delete(point);

						Messages.broadcast(plugin.getServer(),
								"&2Spleef location: &e" + args[0]
										+ "&2 removes");

						return true;
					} else {
						Messages.send(sender, "&cUnknown spleef location: &e"
								+ args[0]);
						return true;
					}
				}
			}

		} else {
			Messages.send(sender,
					"&cYou do not have permission to create spleef areas");
			return true;
		}

		return false;
	}
}
