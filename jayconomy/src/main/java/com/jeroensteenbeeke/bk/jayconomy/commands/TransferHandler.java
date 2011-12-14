package com.jeroensteenbeeke.bk.jayconomy.commands;

import java.math.BigDecimal;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;

public class TransferHandler extends PlayerAwareCommandHandler {
	private final Jayconomy plugin;

	public TransferHandler(Jayconomy plugin) {
		super(plugin.getServer(), Jayconomy.PERMISSION_TRANSFER);

		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "transfer".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			try {
				BigDecimal amount = new BigDecimal(Integer.parseInt(args[0]));
				OfflinePlayer targetPlayer = plugin.getServer()
						.getOfflinePlayer(args[1]);

				if (targetPlayer != null) {
					BigDecimal balance = plugin.getBalance(player.getName());

					if (balance.compareTo(amount) >= 0) {
						plugin.increaseBalance(targetPlayer.getName(), amount);
						plugin.decreaseBalance(player.getName(), amount);
						Messages.send(player, String.format(
								"&aTransfered &e%s&a to &e%s",
								plugin.formatCurrency(amount),
								targetPlayer.getName()));
					} else {
						Messages.send(player,
								"&cYou do not have enough money, you only have &e"
										+ plugin.formatCurrency(balance));
					}

				} else {
					Messages.send(player, "&cUnknown player: &e" + args[0]);
				}

			} catch (NumberFormatException nfe) {
				Messages.send(player, "&cInvalid amount: &e" + args[0]);

			}
			return true;
		}

		return false;
	}

}
