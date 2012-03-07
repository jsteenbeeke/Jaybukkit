package com.jeroensteenbeeke.bk.jayclaim.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.jayclaim.JayClaim;

public class InfoCommand extends PlayerAwareCommandHandler {
	private final JayClaim jayClaim;

	private static final String COMMAND_ARG = "info";

	public InfoCommand(JayClaim jayClaim) {
		super(jayClaim.getServer(), JayClaim.PERMISSION_USE);
		this.jayClaim = jayClaim;
	}

	@Override
	public CommandMatcher getMatcher() {

		return ifNameIs(JayClaim.COMMAND_BASE).andArgIs(0, COMMAND_ARG)
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).andArgumentEquals(0, COMMAND_ARG).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

	}

}
