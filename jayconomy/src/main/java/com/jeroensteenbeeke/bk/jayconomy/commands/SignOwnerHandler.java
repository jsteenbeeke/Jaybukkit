package com.jeroensteenbeeke.bk.jayconomy.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;

public class SignOwnerHandler extends PlayerAwareCommandHandler {
	private final Jayconomy plugin;

	public SignOwnerHandler(Jayconomy plugin) {
		super(plugin.getServer(), Jayconomy.PERMISSION_VIEW_OWNER);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "signowner".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

		Block block = player.getTargetBlock(Jayconomy.transparent, 100);

		if (block != null) {

			JayconomySign sign = plugin.getDatabase()
					.createQuery(JayconomySign.class).where()
					.eq("x", block.getX()).eq("y", block.getY())
					.eq("z", block.getZ())
					.eq("world", block.getWorld().getName()).findUnique();

			if (sign != null) {
				Messages.send(player, "&aSign placed by &e" + sign.getOwner());
			} else {
				Messages.send(player,
						"&cYou are not looking at a Jayconomy sign");
			}
			return true;
		} else {
			Messages.send(player, "&cYou are not looking at a Jayconomy sign");
			return true;
		}
	}
}
