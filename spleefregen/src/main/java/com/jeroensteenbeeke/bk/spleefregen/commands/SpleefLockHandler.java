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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefLockHandler implements CommandHandler {

	private final SpleefRegen plugin;

	public SpleefLockHandler(SpleefRegen plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "splock".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(SpleefRegen.PERMISSION_SPLEEF_LOCK)) {
			if (args.length == 1) {
				Player player = plugin.getServer().getPlayerExact(
						sender.getName());
				if (player != null) {
					String name = args[0];

					SpleefPoint point = plugin.getDatabase()
							.createQuery(SpleefPoint.class).where()
							.eq("name", name).findUnique();

					if (point != null) {
						point.setLocked(true);
						plugin.getDatabase().update(point);

						Messages.broadcast(plugin.getServer(),
								"&2Spleef location: &e" + args[0] + "&2 locked");

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
