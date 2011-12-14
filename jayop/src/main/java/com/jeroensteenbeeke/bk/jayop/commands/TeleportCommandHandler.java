package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class TeleportCommandHandler extends PlayerAwareCommandHandler {
	private JayOp plugin;

	public TeleportCommandHandler(JayOp jayop) {
		super(jayop.getServer(), JayOp.PERMISSION_LOCATIONAL);
		this.plugin = jayop;
	}

	@Override
	public boolean matches(Command command, String[] args) {

		return "tp".equals(command);
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			String target = args[0].toLowerCase();
			Player targetPlayer = plugin.getServer().getPlayerExact(target);

			Messages.broadcast(
					plugin.getServer(),
					String.format("Teleporting &e%s&f to &e%s",
							player.getName(), target));

			player.teleport(targetPlayer);

			return true;
		}

		return false;
	}
}
