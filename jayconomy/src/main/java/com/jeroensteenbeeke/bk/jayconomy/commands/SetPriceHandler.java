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

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign.SignMode;

public class SetPriceHandler extends PlayerAwareCommandHandler {

	private Jayconomy plugin;

	public SetPriceHandler(Jayconomy jayconomy) {
		super(jayconomy.getServer(), Jayconomy.PERMISSION_PLACE);
		this.plugin = jayconomy;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "setprice".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			Block block = player.getTargetBlock(Jayconomy.transparent, 100);

			JayconomySign sign = plugin.getDatabase()
					.createQuery(JayconomySign.class).where()
					.eq("x", block.getX()).eq("y", block.getY())
					.eq("z", block.getZ())
					.eq("world", block.getWorld().getName()).findUnique();

			if (sign != null) {
				if (sign.getSignMode() == SignMode.BUY
						|| sign.getSignMode() == SignMode.SELL) {
					if (sign.getSignMode() == SignMode.SELL
							&& sign.getMax() == null) {
						Messages.send(player,
								"&cCannot set the value of server-controlled substances");
					} else {

						if (player.getName().equals(sign.getOwner())) {
							try {
								int price = Integer.parseInt(args[0]);

								sign.setValue(new BigDecimal(price));

								plugin.getDatabase().update(sign);

								if (sign.getSignMode() == SignMode.SELL) {
									plugin.updateSellSign(
											(Sign) block.getState(), sign);
								} else {
									plugin.updateBuySign(
											(Sign) block.getState(), sign);
								}

							} catch (NumberFormatException nfe) {
								Messages.send(player, "&cUnreadable price: &e"
										+ args[0]);
							}
						} else {
							Messages.send(player, "&cYou do not own this sign");
						}
					}
				} else {
					Messages.send(player,
							"&cCan only use this command on Buy and Sell signs");
				}
			} else {
				Messages.send(player,
						"&cYou are not looking at a Jayconomy sign");
			}

			return true;
		}

		return false;
	}

}
