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
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class TeleportToMeCommandHandler extends PlayerAwareCommandHandler {

	public TeleportToMeCommandHandler(JayOp jayop) {
		super(jayop.getServer(), JayOp.PERMISSION_LOCATIONAL);
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
	public void onAuthorizedAndPlayerFound(Player targetPlayer,
			Command command, String label, String[] args) {

		String subject = args[0].toLowerCase();
		Player subjectPlayer = targetPlayer.getServer().getPlayerExact(subject);

		if (subjectPlayer != null) {
			Messages.broadcast(
					targetPlayer.getServer(),
					String.format("Teleporting &e%s&f to &e%s",
							subjectPlayer.getName(), targetPlayer.getName()));

			subjectPlayer.teleport(targetPlayer);
		} else {
			Messages.send(targetPlayer, String.format(
					"&cPlayer &e%s&c not known or not online", args[0]));
		}
	}

}
