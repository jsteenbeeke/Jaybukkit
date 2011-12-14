package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;
import com.jeroensteenbeeke.bk.jayop.entities.Suspension;

public class SuspendCommandHandler extends PermissibleCommandHandler {
	private final JayOp plugin;

	public SuspendCommandHandler(JayOp plugin) {
		super(JayOp.PERMISSION_ENFORCEMENT);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "suspend".equals(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 2) {
			OfflinePlayer player = plugin.getServer().getOfflinePlayer(
					args[0].toLowerCase());

			if (player != null) {
				for (Suspension suspension : plugin.getDatabase()
						.find(Suspension.class).where()
						.eq("playerName", player.getName()).findList()) {
					plugin.getDatabase().delete(suspension);
				}

				try {
					int days = Integer.parseInt(args[1]);

					Suspension suspension = new Suspension();
					suspension.setStart(System.currentTimeMillis());
					suspension.setDuration(1000 * 60 * 60 * 24 * days);
					suspension.setPlayerName(player.getName());
					plugin.getDatabase().save(suspension);

					Player p = plugin.getServer().getPlayerExact(
							player.getName());
					if (p != null) {
						p.kickPlayer("You have been suspended for " + days
								+ " days");
					}

					return true;
				} catch (NumberFormatException nfe) {
					Messages.send(sender, "&cInvalid number of days &e"
							+ args[1]);
					return true;
				}
			} else {
				Messages.send(sender, "&cUnknown player &e" + args[0]);
				return true;
			}
		}

		return false;
	}

}
