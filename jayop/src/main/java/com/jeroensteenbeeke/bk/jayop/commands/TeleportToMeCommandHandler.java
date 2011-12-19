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
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class TeleportToMeCommandHandler extends PermissibleCommandHandler {

	public TeleportToMeCommandHandler() {
		super(JayOp.PERMISSION_LOCATIONAL);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("tptome").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).andArgumentIsValidPlayerName(0).itIsProper();
	}

	@Override
	public void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		String subject = args[0].toLowerCase();
		Player subjectPlayer = sender.getServer().getPlayerExact(subject);
		Player targetPlayer = sender.getServer().getPlayerExact(
				sender.getName());

		Messages.broadcast(
				sender.getServer(),
				String.format("Teleporting &e%s&f to &e%s",
						subjectPlayer.getName(), targetPlayer.getName()));

		subjectPlayer.teleport(targetPlayer);
	}

}
