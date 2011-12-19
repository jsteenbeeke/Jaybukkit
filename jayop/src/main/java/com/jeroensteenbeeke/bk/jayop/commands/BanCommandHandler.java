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

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class BanCommandHandler extends PermissibleCommandHandler {
	public BanCommandHandler() {
		super(JayOp.PERMISSION_ENFORCEMENT);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ban").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountAtLeast(2).itIsProper();
	}

	@Override
	public void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		OfflinePlayer target = sender.getServer().getOfflinePlayer(args[0]);
		StringBuilder reason = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			if (i > 1) {
				reason.append(" ");
			}
			reason.append(args[i]);
		}

		if (target != null) {
			target.setBanned(true);

			Player player = sender.getServer().getPlayerExact(
					args[0].toLowerCase());
			if (player != null) {
				player.setBanned(true);
				player.kickPlayer("You have been banned: " + reason.toString());
			}

			Messages.broadcast(sender.getServer(), "&cPlayer &e" + args[0]
					+ "&c has been banned");
		} else {
			Messages.send(sender, "&cUnknown player: " + args[0]);
		}

	}

}
