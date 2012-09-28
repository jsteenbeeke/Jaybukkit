package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class SetSpawnLocationCommandHandler extends PlayerAwareCommandHandler {
	public SetSpawnLocationCommandHandler(JayOp jayop) {
		super(jayop.getServer(), JayOp.PERMISSION_MODIFY_WORLD);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("spawn").andArgIs(0, "set").itMatches();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		Location loc = player.getLocation();
		World world = loc.getWorld();

		if (world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(),
				loc.getBlockZ())) {
			Messages.send(player, "&aSpawn location set to current location");
		} else {
			Messages.send(player, "&cUnsuitable location");
		}

	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).andArgumentEquals(0, "set").itIsProper();
	}

}
