package com.jeroensteenbeeke.bk.jaylock.commands;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jaylock.Jaylock;
import com.jeroensteenbeeke.bk.jaylock.entities.ForbiddenLocation;
import com.jeroensteenbeeke.bk.jaylock.jobs.SetOwnerTask;

public class SetLocationOwnerCommand extends PlayerAwareCommandHandler {
	private final Jaylock jaylock;

	public SetLocationOwnerCommand(Jaylock jaylock) {
		super(jaylock.getServer(), Jaylock.PERMISSION_JAYLOCK_ADMIN);
		this.jaylock = jaylock;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("jaylock").andArgIs(0, "set").andArgIs(1, "owner")
				.itMatches();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		Block b = player.getLocation().getBlock();

		ForbiddenLocation loc = jaylock.getForbiddenLocation(b);

		if (loc != null) {
			try {
				List<Block> targetBlocks = jaylock.getTargetBlocks(b);

				jaylock.getServer()
						.getScheduler()
						.scheduleAsyncDelayedTask(
								jaylock,
								new SetOwnerTask(jaylock, args[2], targetBlocks),
								200L);

				Messages.send(player,

				"&cLocation will be marked as private in &e10&c seconds. GET OUT OF THERE!");

			} catch (UnsuitableLocationException ule) {
				Messages.send(
						player,
						String.format(
								"&cThis location is not suitable to mark forbidden: &e%s",
								ule.getMessage()));
			}
		} else {
			Messages.send(player,
					"&cThis location is not yet marked as forbidden");
		}

	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(3).andArgumentIsValidPlayerName(2).itIsProper();
	}

}
