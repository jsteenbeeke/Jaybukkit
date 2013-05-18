package com.jeroensteenbeeke.bukkit;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;

public class PlayerDeathListener implements Listener {
	public static class BanPlayerTask implements Runnable {
		private final Server server;

		private final Player player;

		public BanPlayerTask(Server server, Player player) {
			super();
			this.server = server;
			this.player = player;
		}

		@Override
		public void run() {
			player.kickPlayer("Your death has resulted in your exile");
			player.setBanned(true);
			server.savePlayers();
		}

	}

	private final EnderQuest enderQuest;

	public PlayerDeathListener(EnderQuest enderQuest) {
		super();
		this.enderQuest = enderQuest;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		Messages.send(player, "&cYou have &e1 minute&c to say your farewells");

		enderQuest
				.getServer()
				.getScheduler()
				.scheduleSyncDelayedTask(enderQuest,
						new BanPlayerTask(enderQuest.getServer(), player),
						20 * 60);
	}
}
