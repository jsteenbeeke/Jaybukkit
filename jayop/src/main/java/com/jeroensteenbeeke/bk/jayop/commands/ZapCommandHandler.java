package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class ZapCommandHandler extends PermissibleCommandHandler {

	private final class LightningStrike implements Runnable {
		private final Player player;

		public LightningStrike(Player player) {
			super();
			this.player = player;
		}

		@Override
		public void run() {
			player.getWorld().strikeLightning(player.getLocation());

		}

	}

	private final JayOp jayop;

	public ZapCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_ENFORCEMENT);
		this.jayop = jayop;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("zap").itMatches();
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
			Runnable zap = new LightningStrike(player);
			for (long d = 1L; d <= 10L; d++) {
				jayop.getServer().getScheduler()
						.scheduleSyncDelayedTask(jayop, zap, d * 20L);
			}

			Messages.send(
					player,
					String.format("&cYou seem to have angered &e%s",
							sender.getName()));
		} else {
			Messages.send(sender,
					String.format("&cInvalid target &e%s", target));
		}

	}
}
