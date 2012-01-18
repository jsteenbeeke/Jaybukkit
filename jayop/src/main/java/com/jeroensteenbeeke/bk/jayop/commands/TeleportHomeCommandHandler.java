package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class TeleportHomeCommandHandler extends PermissibleCommandHandler {
	private JayOp jayop;

	public TeleportHomeCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_LOCATIONAL);
		this.jayop = jayop;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("tphome").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).andArgumentIsValidPlayerName(0).itIsProper();
	}

	@Override
	public void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		String target = args[0];

		Player player = jayop.getServer().getPlayerExact(target);

		if (player != null) {
			Location spawn = player.getBedSpawnLocation();
			if (spawn == null) {
				for (World w : jayop.getServer().getWorlds()) {
					switch (w.getEnvironment()) {
					case NORMAL:
						spawn = w.getSpawnLocation();
						break;
					default:
						continue;
					}
				}

			}

			if (spawn != null) {
				player.teleport(spawn, TeleportCause.COMMAND);
				Messages.send(sender, String.format(
						"&aYou were teleported to your home location by &e%s",
						sender.getName()));
			} else {
				Messages.send(
						sender,
						String.format(
								"&cCannot teleport &e%s&c, no suitable home location found",
								target));
			}

		} else {
			Messages.send(sender,
					String.format("&cInvalid target &e%s", target));
		}

	}

}
