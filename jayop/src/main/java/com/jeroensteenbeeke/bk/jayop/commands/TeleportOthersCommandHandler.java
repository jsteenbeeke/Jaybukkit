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

public class TeleportOthersCommandHandler extends PermissibleCommandHandler {
	private JayOp jayop;

	public TeleportOthersCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_LOCATIONAL);
		this.jayop = jayop;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("tpp").itMatches();
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			String subject = args[0].toLowerCase();
			String target = args[1].toLowerCase();

			Player toTeleport = sender.getServer().getPlayerExact(subject);
			Player teleportTo = sender.getServer().getPlayerExact(target);

			if (toTeleport != null && teleportTo != null) {
				toTeleport.teleport(teleportTo);

				Messages.broadcast(jayop.getServer(), "Teleporting " + subject
						+ " to " + target);

			} else {
				if (toTeleport == null) {
					Messages.send(sender, "&cUnknown player: &e" + subject
							+ "&f");

				}
				if (teleportTo == null) {
					Messages.send(sender, "&cUnknown player: &e" + target
							+ "&f");

				}
			}

			return true;
		}

		return false;
	}
}
