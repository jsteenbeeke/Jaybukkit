package com.jeroensteenbeeke.bk.playerbasics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.playerbasics.PlayerBasics;
import com.jeroensteenbeeke.bk.playerbasics.util.PlayerUtil;

public class PlayerListHandler implements CommandHandler {

	@Override
	public boolean matches(Command command, String[] args) {
		return "list".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(PlayerBasics.PERMISSION_LIST)) {
			StringBuilder online = new StringBuilder();
			online.append("&fThe following players are online: [");
			online.append(PlayerUtil.compilePlayerList(sender.getServer()));
			online.append("&f]");

			Messages.send(sender, online.toString());

			return true;

		} else {
			sender.sendMessage("\u00A7cYou do not have permission to use this command\u00A7f");
			return true;
		}
	}

}
