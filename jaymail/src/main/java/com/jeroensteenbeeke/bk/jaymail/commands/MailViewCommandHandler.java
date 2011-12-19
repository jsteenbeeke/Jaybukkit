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
package com.jeroensteenbeeke.bk.jaymail.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jaymail.JaymailPlugin;
import com.jeroensteenbeeke.bk.jaymail.entities.JayMail;

public class MailViewCommandHandler extends PlayerAwareCommandHandler {
	private JaymailPlugin plugin;

	public MailViewCommandHandler(JaymailPlugin plugin) {
		super(plugin.getServer(), JaymailPlugin.PERMISSION_MAIL);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("mail").andArgIs(0, "read").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {

		return ifArgCountIs(2).andArgumentEquals(0, "read")
				.andArgumentLike(1, DECIMAL).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		Long id = Long.parseLong(args[1]);

		JayMail mail = plugin.getDatabase().find(JayMail.class).where()
				.eq("recipient", player.getName()).eq("id", id).findUnique();

		if (mail != null) {
			Messages.send(
					player,
					String.format("&aFrom &e%s&a: %s", mail.getSender(),
							mail.getMessage()));
		} else {
			Messages.send(player, "&cInvalid mail number: &e" + id);
		}
	}
}
