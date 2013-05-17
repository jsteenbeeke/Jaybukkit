package com.jeroensteenbeeke.bukkit;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;

public class EnderQuestCommand extends PlayerAwareCommandHandler {
	public class BroadCastTimer implements Runnable {
		private final Server server;

		private final String message;

		public BroadCastTimer(Server server, String message) {
			this.server = server;
			this.message = message;
		}

		@Override
		public void run() {
			Messages.broadcast(server, message);
		}

	}

	private final EnderQuest quest;

	public EnderQuestCommand(EnderQuest quest) {
		super(quest.getServer(), EnderQuest.PERMISSION);
		this.quest = quest;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("enderquest").andArgLike(0, DECIMAL).itMatches();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String time = args[0];
		int minutes = Integer.parseInt(time);

		Server server = player.getServer();

		broadcastStartingMessage(minutes, server);
		spawnPortal(player);
		startCountdown(server, minutes);
	}

	private void startCountdown(Server server, int minutes) {
		int remaining = minutes;

		while (remaining > 30) {
			remaining = remaining - 30;

			long time = (minutes - remaining) * 60 * 20;

			server.getScheduler()
					.runTaskLater(
							quest,
							new BroadCastTimer(
									server,
									String.format(
											"&cYou have &e%d minutes&c left to complete your quest",
											remaining)), time);

		}

		while (remaining > 1) {
			long time = (minutes - remaining) * 60 * 20;

			server.getScheduler()
					.runTaskLater(
							quest,
							new BroadCastTimer(
									server,
									String.format(
											"&cYou only have &e%d minutes&c left to complete your quest",
											remaining)), time);

			remaining = remaining - 1;
		}

		server.getScheduler().runTaskLater(quest,
				new BroadCastTimer(server, "&cTIME IS UP!!!"),
				minutes * 60 * 20);
	}

	private void spawnPortal(Player player) {

		Block block = player.getTargetBlock(
				Sets.<Byte> newHashSet((byte) Material.AIR.getId()), 50);
		World world = player.getWorld();

		final int y = block.getY();

		final int lowerX = block.getX() - 2;
		final int upperX = block.getX() + 2;
		final int lowerZ = block.getZ() - 2;
		final int upperZ = block.getZ() + 2;

		for (int x = lowerX; x <= upperX; x++) {
			for (int z = lowerZ; z <= upperZ; z++) {
				if (x == lowerX || x == upperX || z == lowerZ || z == upperZ) {
					world.getBlockAt(x, y, z).setTypeIdAndData(
							Material.ENDER_PORTAL_FRAME.getId(), (byte) 0x4,
							false);
				} else {
					world.getBlockAt(x, y, z).setType(Material.ENDER_PORTAL);
				}
			}
		}

		world.getBlockAt(lowerX, y, lowerZ).setType(Material.AIR);
		world.getBlockAt(lowerX, y, upperZ).setType(Material.AIR);
		world.getBlockAt(upperX, y, lowerZ).setType(Material.AIR);
		world.getBlockAt(upperX, y, upperZ).setType(Material.AIR);

	}

	private void broadcastStartingMessage(Integer minutes, Server server) {
		Messages.broadcast(server,
				"&cThe quest for the &eEnder Dragon &cis now commencing");
		Messages.broadcast(server, String.format(
				"&cYou now have &e%d minutes&c to kill the Ender Dragon",
				minutes));
		Messages.broadcast(server, "&cGood luck, and keep the traditions alive");
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).andArgumentLike(0, DECIMAL).itIsProper();
	}

}
