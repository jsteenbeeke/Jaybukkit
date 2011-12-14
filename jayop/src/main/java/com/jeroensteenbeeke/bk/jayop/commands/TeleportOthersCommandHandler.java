package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class TeleportOthersCommandHandler extends PermissibleCommandHandler {
	private JayOp jayop;

	public TeleportOthersCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_LOCATIONAL);
		this.jayop = jayop;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "tpp".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			String subject = args[0].toLowerCase();
			String target = args[1].toLowerCase();

			Player toTeleport = sender.getServer().getPlayerExact(subject);
			Player teleportTo = sender.getServer().getPlayerExact(target);

			if (toTeleport != null && teleportTo != null) {
				toTeleport.teleport(teleportTo);

				Messages.broadcast(jayop.getServer(), "Teleporting " + subject
						+ " to " + target);

			} else {
				if (toTeleport == null) {
					Messages.send(sender, "&cUnknown player: &e" + subject
							+ "&f");

				}
				if (teleportTo == null) {
					Messages.send(sender, "&cUnknown player: &e" + target
							+ "&f");

				}
			}

			return true;
		}

		return false;
	}
}
