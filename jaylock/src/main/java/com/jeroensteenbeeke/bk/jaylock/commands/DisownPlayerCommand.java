package com.jeroensteenbeeke.bk.jaylock.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jaylock.Jaylock;

public class DisownPlayerCommand extends PlayerAwareCommandHandler {
	private final Jaylock jaylock;

	public DisownPlayerCommand(Jaylock jaylock) {
		super(jaylock.getServer(), Jaylock.PERMISSION_JAYLOCK_ADMIN);
		this.jaylock = jaylock;
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		int execute = jaylock
				.getDatabase()
				.createSqlUpdate(
						"UPDATE ForbiddenLocation SET owner=null WHERE owner=:owner")
				.setParameter("owner", args[1]).execute();

		Messages.send(player, String.format("&a%d blocks updated", execute));

	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("jaylock").andArgIs(0, "disown").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).andArgumentEquals(0, "disown")
				.andArgumentIsValidPlayerName(1).itIsProper();
	}

}
