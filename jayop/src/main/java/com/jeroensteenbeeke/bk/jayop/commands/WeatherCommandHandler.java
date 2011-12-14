package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class WeatherCommandHandler implements CommandHandler {
	@Override
	public boolean matches(Command command, String[] args) {
		return "weather".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			if (sender.hasPermission(JayOp.PERMISSION_ENVIRONMENT)) {
				Player player = sender.getServer().getPlayerExact(
						sender.getName());

				if ("sun".equals(args[0])) {
					player.getWorld().setStorm(false);
					player.getWorld().setThundering(false);

					sender.getServer().broadcastMessage(
							"Weather changed to \u00A7esun\u00A7f");

					return true;
				}
				if ("rain".equals(args[0]) || "snow".equals(args[0])) {
					player.getWorld().setStorm(true);
					player.getWorld().setThundering(false);

					sender.getServer().broadcastMessage(
							"Weather changed to \u00A7erain and snow\u00A7f");

					return true;
				}
				if ("thunderstorm".equals(args[0])) {
					sender.getServer().broadcastMessage(
							"Weather changed to \u00A7ethunderstorm\u00A7f");

					player.getWorld().setStorm(true);
					player.getWorld().setThundering(true);

					return true;
				}

				return false;
			} else {
				sender.sendMessage("\u00A7cYou do not have permission to change the weather\u00A7f");
				return true;
			}
		}

		return false;
	}
}
