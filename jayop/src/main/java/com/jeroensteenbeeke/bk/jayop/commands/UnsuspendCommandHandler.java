package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.jayop.JayOp;
import com.jeroensteenbeeke.bk.jayop.entities.Suspension;

public class UnsuspendCommandHandler implements CommandHandler {
	private final JayOp plugin;

	public UnsuspendCommandHandler(JayOp plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "unsuspend".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(JayOp.PERMISSION_ENFORCEMENT)) {
			if (args.length == 1) {
				OfflinePlayer player = plugin.getServer().getOfflinePlayer(
						args[0]);

				if (player != null) {
					for (Suspension suspension : plugin.getDatabase()
							.find(Suspension.class).where()
							.eq("playerName", player.getName()).findList()) {
						plugin.getDatabase().delete(suspension);
					}
				} else {
					sender.sendMessage("\u00A7cUnknown player \u00A7e"
							+ args[0] + "\u00A7f");
					return true;
				}
			}
		} else {
			sender.sendMessage("\u00A7cYou do not have permission to lift suspensions\u00A7f");
			return true;
		}

		return false;
	}

}
