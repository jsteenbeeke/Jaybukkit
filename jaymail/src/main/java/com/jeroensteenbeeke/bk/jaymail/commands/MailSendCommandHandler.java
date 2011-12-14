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

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.jaymail.JaymailPlugin;
import com.jeroensteenbeeke.bk.jaymail.entities.JayMail;

public class MailSendCommandHandler extends PlayerAwareCommandHandler {
	private JaymailPlugin plugin;

	public MailSendCommandHandler(JaymailPlugin plugin) {
		super(plugin.getServer(), JaymailPlugin.PERMISSION_MAIL);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "mail-send".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length >= 2) {
			String recipient = args[0];

			StringBuilder message = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				if (i > 1) {
					message.append(" ");
				}

				message.append(args[i]);
			}

			JayMail mail = new JayMail();
			mail.setMessage(message.toString());
			mail.setRecipient(recipient);
			mail.setSender(player.getName());
			plugin.getDatabase().save(mail);

			return true;
		}

		return false;
	}

}
