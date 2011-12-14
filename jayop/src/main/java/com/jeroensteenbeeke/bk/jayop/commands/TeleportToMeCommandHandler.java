package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class TeleportToMeCommandHandler extends PermissibleCommandHandler {

	public TeleportToMeCommandHandler() {
		super(JayOp.PERMISSION_LOCATIONAL);
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "tptome".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 1) {
			String subject = args[0].toLowerCase();
			Player subjectPlayer = sender.getServer().getPlayerExact(subject);
			Player targetPlayer = sender.getServer().getPlayerExact(
					sender.getName());

			Messages.broadcast(
					sender.getServer(),
					String.format("Teleporting &e%s&f to &e%s",
							subjectPlayer.getName(), targetPlayer.getName()));

			subjectPlayer.teleport(targetPlayer);

			return true;
		}

		return false;
	}

}
