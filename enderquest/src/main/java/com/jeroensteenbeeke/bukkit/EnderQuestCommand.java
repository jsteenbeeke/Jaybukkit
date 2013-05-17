package com.jeroensteenbeeke.bukkit;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;

public class EnderQuestCommand extends PlayerAwareCommandHandler {
	public static class KillEveryoneTimer implements Runnable {

		private final Server server;

		public KillEveryoneTimer(Server server) {
			this.server = server;
		}

		@Override
		public void run() {
			Player[] players = server.getOnlinePlayers();

			for (Player player : players) {
				player.damage(10000);
			}
		}

	}

	public static class Notification {
		private final long moment;

		private final int minutes;

		public Notification(long moment, int minutes) {
			super();
			this.moment = moment;
			this.minutes = minutes;
		}

		public long getMoment() {
			return moment;
		}

		public int getMinutes() {
			return minutes;
		}
	}

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
		List<Notification> notificationMoments = getNotificationTimes(minutes);

		for (Notification notification : notificationMoments) {
			server.getScheduler()
					.runTaskLater(
							quest,
							new BroadCastTimer(
									quest.getServer(),
									String.format(
											"&cYou have &e%d minutes&c left to complete your quest",
											notification.minutes)),
							notification.moment);
		}

		server.getScheduler().runTaskLater(quest,
				new BroadCastTimer(server, "&cONLY ONE MINUTE REMAINS"),
				20 * 60 * (minutes - 1));

		server.getScheduler().runTaskLater(quest,
				new KillEveryoneTimer(server), minutes * 60 * 20);
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

	public static List<Notification> getNotificationTimes(final int minutes) {
		List<Notification> notifications = Lists.newLinkedList();

		if (minutes > 5) {
			for (int i = 2; i <= 5; i++) {
				notifications.add(minutes(minutes - i, i));
			}
		}

		if (minutes > 15) {
			notifications.add(minutes(minutes - 15, 15));
		}

		if (minutes > 30) {
			for (int i = 30; i < minutes; i = i + 30) {
				notifications.add(minutes(minutes - i, i));
			}
		}

		return notifications;
	}

	/**
	 * Convert minutes to server ticks
	 */
	private static Notification minutes(int minutes, int remaining) {
		return new Notification(20 * 60 * minutes, remaining);
	}
}
