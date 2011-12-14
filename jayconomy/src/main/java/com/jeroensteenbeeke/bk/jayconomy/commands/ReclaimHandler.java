package com.jeroensteenbeeke.bk.jayconomy.commands;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign.SignMode;

public class ReclaimHandler extends PlayerAwareCommandHandler {
	private Jayconomy plugin;

	public ReclaimHandler(Jayconomy jayconomy) {
		super(jayconomy.getServer(), Jayconomy.PERMISSION_PLACE);
		this.plugin = jayconomy;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "reclaim".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		Block block = player.getTargetBlock(Jayconomy.transparent, 100);

		JayconomySign sign = plugin.getDatabase()
				.createQuery(JayconomySign.class).where().eq("x", block.getX())
				.eq("y", block.getY()).eq("z", block.getZ())
				.eq("world", block.getWorld().getName()).findUnique();

		if (sign != null) {
			if (sign.getSignMode() == SignMode.SELL) {
				if (sign.getMax() == null) {
					Messages.send(player,
							"&cCannot retrieve server-controlled substances");
				} else {

					if (player.getName().equals(sign.getOwner())) {
						if (sign.getMax() > 0) {
							sign.setMax(sign.getMax() - 1);

							plugin.getDatabase().update(sign);

							player.getInventory().addItem(
									plugin.getSignStack(sign));

							if (sign.getSignMode() == SignMode.SELL) {
								plugin.updateSellSign((Sign) block.getState(),
										sign);
							} else {
								plugin.updateBuySign((Sign) block.getState(),
										sign);
							}
						} else {
							Messages.send(player, "&cThis sign is empty");
						}
					} else {
						Messages.send(player, "&cYou do not own this sign");
					}
				}
			} else {
				Messages.send(player,
						"&cCan only use this command on Sell signs");
			}
		} else {
			Messages.send(player, "&cYou are not looking at a Jayconomy sign");
		}

		return true;
	}
}
