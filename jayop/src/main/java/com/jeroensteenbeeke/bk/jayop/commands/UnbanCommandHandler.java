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

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class UnbanCommandHandler extends PermissibleCommandHandler {
	public UnbanCommandHandler() {
		super(JayOp.PERMISSION_ENFORCEMENT);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("unban").itMatches();
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 1) {
			OfflinePlayer target = sender.getServer().getOfflinePlayer(args[0]);

			if (target != null) {
				target.setBanned(false);

				Messages.send(sender, "&cPlayer &e" + args[0]
						+ "&c has been unbanned");

				return true;
			} else {
				Messages.send(sender, "&cUnknown player: " + args[0] + "");

				return true;
			}

		}

		return false;
	}

}
