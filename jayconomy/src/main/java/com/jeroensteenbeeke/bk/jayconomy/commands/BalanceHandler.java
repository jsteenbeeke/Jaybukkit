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
package com.jeroensteenbeeke.bk.jayconomy.commands;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.avaje.ebean.Transaction;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.Balance;

public class BalanceHandler extends PermissibleCommandHandler {
	private final Jayconomy plugin;

	public BalanceHandler(Jayconomy plugin) {
		super(Jayconomy.PERMISSION_BALANCE);
		this.plugin = plugin;

	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "balance".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		Transaction t = plugin.getDatabase().beginTransaction();

		Balance balance = plugin.getDatabase().createQuery(Balance.class)
				.where().eq("playerName", sender.getName()).findUnique();

		BigDecimal amount = BigDecimal.ZERO;
		if (balance != null) {
			amount = balance.getBalance();
		} else {
			balance = new Balance();
			balance.setBalance(amount);
			balance.setPlayerName(sender.getName());
			plugin.getDatabase().save(balance, t);
		}

		Messages.send(sender,
				"&cYour balance is: &e" + plugin.formatCurrency(amount));

		t.commit();

		return true;
	}
}
