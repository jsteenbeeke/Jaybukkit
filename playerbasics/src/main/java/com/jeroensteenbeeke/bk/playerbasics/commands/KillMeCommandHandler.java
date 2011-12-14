package com.jeroensteenbeeke.bk.playerbasics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;

public class KillMeCommandHandler implements CommandHandler {

	@Override
	public boolean matches(Command command, String[] args) {
		return command.equals("killme");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = sender.getServer().getPlayerExact(sender.getName());

		if (player != null) {
			player.damage(10000);
			return true;
		}

		return false;
	}

}
