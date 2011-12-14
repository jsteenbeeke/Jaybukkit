package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class BanCommandHandler extends PermissibleCommandHandler {
	public BanCommandHandler() {
		super(JayOp.PERMISSION_ENFORCEMENT);
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "ban".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length > 1) {
			OfflinePlayer target = sender.getServer().getOfflinePlayer(args[0]);
			StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				if (i > 1) {
					reason.append(" ");
				}
				reason.append(args[i]);
			}

			if (target != null) {
				target.setBanned(true);

				Player player = sender.getServer().getPlayerExact(
						args[0].toLowerCase());
				if (player != null) {
					player.setBanned(true);
					player.kickPlayer("You have been banned: "
							+ reason.toString());
				}

				Messages.broadcast(sender.getServer(), "&cPlayer &e" + args[0]
						+ "&c has been banned");

				return true;
			} else {
				Messages.send(sender, "&cUnknown player: " + args[0]);

				return true;
			}

		}

		return false;
	}

}
