/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class GiveItemCommandHandler extends PermissibleCommandHandler {

	public GiveItemCommandHandler() {
		super(JayOp.PERMISSION_INVENTORY);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("give").itMatches();
	}

	@SuppressWarnings("deprecation")
	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountAtLeast(2).andArgCountAtMost(3)
				.andArgumentIsValidPlayerName(0).itIsProper();
	}

	@Override
	public void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		String playerName = args[0];
		Player player = sender.getServer().getPlayerExact(playerName);

		if (player != null) {
			int quantity = 1;

			PlayerInventory inventory = player.getInventory();

			if (args.length == 3) {
				try {
					quantity = Integer.parseInt(args[2]);
				} catch (NumberFormatException nfe) {
					Messages.send(sender, "&cUnreadable quantity: &e" + args[2]);
				}
			}

			try {
				int itemId = Integer.parseInt(args[1]);
				ItemStack stack = new ItemStack(itemId, quantity);
				inventory.addItem(stack);
				if (stack.getType() != null) {
					Messages.broadcast(sender.getServer(), String.format(
							"Giving &e%s&f of &e%s&f to &e%s", quantity, stack
									.getType().toString(), player
									.getDisplayName()));
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
							"Giving &e%s&f of &e%s&f to &e%s", quantity, stack
									.getType().toString(), player
									.getDisplayName()));
				} else {
					Messages.send(sender, "&cUnknown item &e" + args[1]);
				}
			}
		} else {
			Messages.send(sender, "&cUnknown player &e" + playerName);
		}
	}

}
