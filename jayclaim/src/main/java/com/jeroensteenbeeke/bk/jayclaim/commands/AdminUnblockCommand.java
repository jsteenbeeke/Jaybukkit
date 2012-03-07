package com.jeroensteenbeeke.bk.jayclaim.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.jayclaim.JayClaim;

public class AdminUnblockCommand extends PlayerAwareCommandHandler {
	private final JayClaim jayClaim;

	private static final String ARG_ADMIN = "admin";

	private static final String COMMAND_ARG = "unblock";

	public AdminUnblockCommand(JayClaim jayClaim) {
		super(jayClaim.getServer(), JayClaim.PERMISSION_ADMIN);
		this.jayClaim = jayClaim;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs(JayClaim.COMMAND_BASE).andArgIs(0, ARG_ADMIN)
				.andArgIs(1, COMMAND_ARG).itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).andArgumentEquals(0, ARG_ADMIN)
				.andArgumentEquals(1, COMMAND_ARG).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

	}

}
