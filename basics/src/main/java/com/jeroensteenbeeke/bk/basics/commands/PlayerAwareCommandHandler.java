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

	public abstract boolean onAuthorizedAndPlayerFound(Player player,
			Command command, String label, String[] args);

	@Override
	public final boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = server.getPlayerExact(sender.getName());

		if (player != null) {
			return onAuthorizedAndPlayerFound(player, command, label, args);
		} else {
			Messages.send(sender,
					"&cYou are not capable of performing this action");
			return true;
		}
	}

}
