package com.jeroensteenbeeke.bk.spleefregen.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefUnlockHandler implements CommandHandler {

	private final SpleefRegen plugin;

	public SpleefUnlockHandler(SpleefRegen plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "unsplock".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(SpleefRegen.PERMISSION_SPLEEF_LOCK)) {
			if (args.length == 1) {
				Player player = plugin.getServer().getPlayerExact(
						sender.getName());
				if (player != null) {
					String name = args[0];

					SpleefPoint point = plugin.getDatabase()
							.createQuery(SpleefPoint.class).where()
							.eq("name", name).findUnique();

					if (point != null) {
						point.setLocked(false);
						plugin.getDatabase().update(point);

						Messages.broadcast(plugin.getServer(),
								"&2Spleef location: &e" + args[0]
										+ "&2 unlocked");

						return true;
					} else {
						Messages.send(sender, "&cUnknown spleef location: &e"
								+ args[0]);
						return true;
					}
				}
			}

		} else {
			Messages.send(sender,
					"&cYou do not have permission to create spleef areas");
			return true;
		}

		return false;
	}

}
