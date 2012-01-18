package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class FreezeCommandHandler extends PermissibleCommandHandler {

	private final JayOp jayop;

	public FreezeCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_ENFORCEMENT);
		this.jayop = jayop;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("freeze").itMatches();
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
			jayop.freeze(player);

			Messages.send(sender,
					String.format("Player &e%s&f now on frozen", target));
			Messages.send(player,
					String.format("You were frozen by &e%s", sender.getName()));

		} else {
			Messages.send(sender,
					String.format("&cInvalid target &e%s", target));
		}

	}

}
