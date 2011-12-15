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

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class ClearInventoryCommandHandler extends PermissibleCommandHandler {

	public ClearInventoryCommandHandler() {
		super(JayOp.PERMISSION_INVENTORY);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("clear-inventory").itMatches();
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 1) {
			String playerName = args[0];
			Player player = sender.getServer().getPlayerExact(playerName);

			if (player != null) {
				player.getInventory().clear();
				Messages.send(sender, "&aInventory of &e" + playerName
						+ "&a cleared");
				Messages.send(player, "&cYour inventory has been cleared by &e"
						+ sender.getName());

				return true;
			} else {
				Messages.send(sender, "&cUnknown player &e" + playerName);

				return true;
			}
		}

		return false;
	}

}
