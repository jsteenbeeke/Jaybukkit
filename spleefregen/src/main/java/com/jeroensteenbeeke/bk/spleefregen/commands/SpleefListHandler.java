package com.jeroensteenbeeke.bk.spleefregen.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefListHandler implements CommandHandler {

	private final SpleefRegen plugin;

	public SpleefListHandler(SpleefRegen plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "splist".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(SpleefRegen.PERMISSION_SPLEEF_LIST)) {
			if (args.length == 0) {
				List<SpleefPoint> points = plugin.getDatabase()
						.createQuery(SpleefPoint.class).findList();

				if (!points.isEmpty()) {
					Messages.send(sender, "&2Spleef locations on this server");

					for (SpleefPoint point : points) {
						Messages.send(sender, "  &2- &e" + point.getName()
								+ " &2in " + point.getWorld() + "&e");
					}

					return true;
				} else {
					Messages.send(sender,
							"&cNo spleef locations on this server");
					return true;
				}
			}

		} else {
			Messages.send(sender,
					"&cYou do not have permission to list spleef areas");
			return true;
		}

		return false;
	}

}
