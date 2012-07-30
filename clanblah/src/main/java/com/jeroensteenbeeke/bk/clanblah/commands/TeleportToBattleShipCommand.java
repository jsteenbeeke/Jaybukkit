package com.jeroensteenbeeke.bk.clanblah.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.clanblah.ClanBlah;

public class TeleportToBattleShipCommand extends PlayerAwareCommandHandler {

	private final ClanBlah blah;

	public TeleportToBattleShipCommand(ClanBlah blah) {
		super(blah.getServer(), PermissionPolicy.ALL,
				ClanBlah.PERMISSION_BLAH_MEMBER);
		this.blah = blah;
	}

	@Override
	public CommandMatcher getMatcher() {

		return ifNameIs("blah").andArgIs(0, "follow").andArgIs(1, "the")
				.andArgIs(2, "white").andArgIs(3, "sea").andArgIs(4, "otter")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(5).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		Block battleShipLocation = blah.getBattleShipLocation();

		if (battleShipLocation != null) {
			player.teleport(battleShipLocation.getLocation());
		}

	}
}
