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

public class MarkLocationForbiddenCommand extends PlayerAwareCommandHandler {
	private final Jaylock jaylock;

	public MarkLocationForbiddenCommand(Jaylock jaylock) {
		super(jaylock.getServer(), PermissionPolicy.ANY,
				Jaylock.PERMISSION_JAYLOCK_ADMIN);
		this.jaylock = jaylock;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("jaylock").andArgIs(0, "mark").andArgIs(1, "forbidden")
				.itMatches();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		Block b = player.getLocation().getBlock();

		ForbiddenLocation loc = jaylock.getForbiddenLocation(b);

		if (loc != null) {
			Messages.send(player,
					"&cThis location is already marked as forbidden");
		} else {
			try {
				List<Block> targetBlocks = jaylock.getTargetBlocks(b);

				for (Block t : targetBlocks) {
					if (jaylock.getForbiddenLocation(t) != null)
						throw new UnsuitableLocationException(
								"area already contains forbidden location");
				}

				int i = 0;
				jaylock.getDatabase().beginTransaction();
				for (Block t : targetBlocks) {
					ForbiddenLocation fb = new ForbiddenLocation();
					fb.setOwner(null);
					fb.setWorld(b.getWorld().getName());
					fb.setX(t.getX());
					fb.setY(t.getY());
					fb.setZ(t.getZ());

					jaylock.getDatabase().save(fb);
					i++;

				}
				jaylock.getDatabase().commitTransaction();

				Messages.send(
						player,
						String.format(
								"&aThis location is now marked as forbidden (&e%d&a blocks)",
								i));

			} catch (UnsuitableLocationException ule) {
				Messages.send(
						player,
						String.format(
								"&cThis location is not suitable to mark forbidden: &e%s",
								ule.getMessage()));
			}
		}
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).itIsProper();
	}

}
