package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class GiveItemCommandHandler extends PermissibleCommandHandler {

	public GiveItemCommandHandler() {
		super(JayOp.PERMISSION_INVENTORY);
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "give".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 2 || args.length == 3) {
			String playerName = args[0];
			Player player = sender.getServer().getPlayerExact(playerName);

			if (player != null) {
				int quantity = 1;

				PlayerInventory inventory = player.getInventory();

				if (args.length == 3) {
					try {
						quantity = Integer.parseInt(args[2]);
					} catch (NumberFormatException nfe) {
						Messages.send(sender, "&cUnreadable quantity: &e"
								+ args[2]);
					}
				}

				try {
					int itemId = Integer.parseInt(args[1]);
					ItemStack stack = new ItemStack(itemId, quantity);
					inventory.addItem(stack);
					if (stack.getType() != null) {
						Messages.broadcast(sender.getServer(), String.format(
								"Giving &e%s&f of &e%s&f to &e%s", quantity,
								stack.getType().toString(),
								player.getDisplayName()));
					} else {
						Messages.broadcast(sender.getServer(), String.format(
								"Giving &e%s&f of &e%s&f to &e%s", quantity,
								stack.getTypeId(), player.getDisplayName()));
					}

				} catch (NumberFormatException nfe) {
					Material m = Material.matchMaterial(args[1]);
					if (m != null) {
						ItemStack stack = new ItemStack(m, quantity);
						inventory.addItem(stack);
						Messages.broadcast(sender.getServer(), String.format(
								"Giving &e%s&f of &e%s&f to &e%s", quantity,
								stack.getType().toString(),
								player.getDisplayName()));
					} else {
						Messages.send(sender, "&cUnknown item &e" + args[1]);
					}
				}

				return true;
			} else {
				Messages.send(sender, "&cUnknown player &e" + playerName);

				return true;
			}
		}

		return false;
	}

}
