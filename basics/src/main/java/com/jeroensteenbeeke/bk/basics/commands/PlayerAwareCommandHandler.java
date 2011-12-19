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
package com.jeroensteenbeeke.bk.basics.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.util.Messages;

public abstract class PlayerAwareCommandHandler extends
		PermissibleCommandHandler {

	private final Server server;

	public PlayerAwareCommandHandler(Server server, PermissionPolicy policy,
			String... requiredPermissions) {
		super(policy, requiredPermissions);
		this.server = server;
	}

	public PlayerAwareCommandHandler(Server server,
			String... requiredPermissions) {
		super(requiredPermissions);
		this.server = server;
	}

	public abstract void onAuthorizedAndPlayerFound(Player player,
			Command command, String label, String[] args);

	@Override
	public final void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = server.getPlayerExact(sender.getName());

		if (player != null) {
			onAuthorizedAndPlayerFound(player, command, label, args);
		} else {
			Messages.send(sender,
					"&cYou are not capable of performing this action");
		}
	}

}
