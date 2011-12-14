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

public class SetMaxHandler extends PlayerAwareCommandHandler {

	private Jayconomy plugin;

	public SetMaxHandler(Jayconomy jayconomy) {
		super(jayconomy.getServer(), Jayconomy.PERMISSION_PLACE);
		this.plugin = jayconomy;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "setmax".equals(command.getName());
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
				if (sign.getSignMode() == SignMode.BUY) {
					if (player.getName().equals(sign.getOwner())) {
						try {
							if (args[0].equalsIgnoreCase("none")) {
								sign.setMax(null);
							} else {
								int max = Integer.parseInt(args[0]);

								sign.setMax(max);

							}

							plugin.getDatabase().update(sign);

							plugin.updateBuySign((Sign) block.getState(), sign);
						} catch (NumberFormatException nfe) {
							Messages.send(player, "&cUnreadable max: &e"
									+ args[0]);
						}
					} else {
						Messages.send(player, "&cYou do not own this sign");
					}
				} else {
					Messages.send(player,
							"&cCan only use this command on Buy signs");
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
