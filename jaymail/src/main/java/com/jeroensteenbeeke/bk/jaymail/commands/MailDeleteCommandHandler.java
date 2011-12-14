package com.jeroensteenbeeke.bk.jaymail.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jaymail.JaymailPlugin;
import com.jeroensteenbeeke.bk.jaymail.entities.JayMail;

public class MailDeleteCommandHandler extends PlayerAwareCommandHandler {
	private JaymailPlugin plugin;

	public MailDeleteCommandHandler(JaymailPlugin plugin) {
		super(plugin.getServer(), JaymailPlugin.PERMISSION_MAIL);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "mail-delete".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			try {
				Long id = Long.parseLong(args[0]);

				JayMail mail = plugin.getDatabase().find(JayMail.class).where()
						.eq("recipient", player.getName()).eq("id", id)
						.findUnique();

				if (mail != null) {
					plugin.getDatabase().delete(mail);
					Messages.send(player,
							String.format("&aMail &e%s&a deleted", id));
				} else {
					Messages.send(player, "&cInvalid mail number: &e" + id);
				}
			} catch (NumberFormatException nfe) {
				Messages.send(player, "&cInvalid mail number: &e" + args[0]);
			}

			return true;
		}

		return false;
	}
}
