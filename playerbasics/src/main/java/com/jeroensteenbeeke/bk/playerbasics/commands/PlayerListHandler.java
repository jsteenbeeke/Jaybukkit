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
package com.jeroensteenbeeke.bk.playerbasics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.playerbasics.PlayerBasics;
import com.jeroensteenbeeke.bk.playerbasics.util.PlayerUtil;

public class PlayerListHandler extends PermissibleCommandHandler {

	public PlayerListHandler() {
		super(PlayerBasics.PERMISSION_LIST);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("list").itMatches();
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		StringBuilder online = new StringBuilder();
		online.append("&fThe following players are online: [");
		online.append(PlayerUtil.compilePlayerList(sender.getServer()));
		online.append("&f]");

		Messages.send(sender, online.toString());

		return true;

	}

}
