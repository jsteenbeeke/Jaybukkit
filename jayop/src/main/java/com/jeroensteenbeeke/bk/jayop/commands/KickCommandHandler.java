package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class KickCommandHandler extends PermissibleCommandHandler {
	public KickCommandHandler() {
		super(JayOp.PERMISSION_ENFORCEMENT);
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "kick".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length > 1) {
			StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				if (i > 1) {
					reason.append(" ");
				}
				reason.append(args[i]);
			}

			Player player = sender.getServer().getPlayerExact(args[0]);
			if (player != null) {
				player.kickPlayer(reason.toString());
			} else {
				Messages.send(sender, "&cUnknown player: " + args[0]);

				return true;
			}

		}

		return false;
	}

}
