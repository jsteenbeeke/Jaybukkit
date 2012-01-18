package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class UnfreezeCommandHandler extends PermissibleCommandHandler {

	private final JayOp jayop;

	public UnfreezeCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_ENFORCEMENT);
		this.jayop = jayop;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("unfreeze").itMatches();
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
			jayop.unfreeze(player);

			Messages.send(sender,
					String.format("Player &e%s&f now on unfrozen", target));
			Messages.send(player, String.format("You were unfrozen by &e%s",
					sender.getName()));

		} else {
			Messages.send(sender,
					String.format("&cInvalid target &e%s", target));
		}
	}

}
