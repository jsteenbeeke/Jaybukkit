package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class ClearInventoryCommandHandler extends PermissibleCommandHandler {

	public ClearInventoryCommandHandler() {
		super(JayOp.PERMISSION_INVENTORY);
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "clear-inventory".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 1) {
			String playerName = args[0];
			Player player = sender.getServer().getPlayerExact(playerName);

			if (player != null) {
				player.getInventory().clear();
				Messages.send(sender, "&aInventory of &e" + playerName
						+ "&a cleared");
				Messages.send(player, "&cYour inventory has been cleared by &e"
						+ sender.getName());

				return true;
			} else {
				Messages.send(sender, "&cUnknown player &e" + playerName);

				return true;
			}
		}

		return false;
	}

}
