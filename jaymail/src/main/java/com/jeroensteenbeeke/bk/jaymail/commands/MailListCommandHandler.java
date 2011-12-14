package com.jeroensteenbeeke.bk.jaymail.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.jaymail.JaymailPlugin;

public class MailListCommandHandler extends PlayerAwareCommandHandler {
	private JaymailPlugin plugin;

	public MailListCommandHandler(JaymailPlugin plugin) {
		super(plugin.getServer(), JaymailPlugin.PERMISSION_MAIL);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "mail".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		plugin.viewMails(player);

		return true;
	}

}
