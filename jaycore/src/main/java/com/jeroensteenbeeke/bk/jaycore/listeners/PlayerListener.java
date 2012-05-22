package com.jeroensteenbeeke.bk.jaycore.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.avaje.ebean.EbeanServer;
import com.jeroensteenbeeke.bk.jaycore.entities.DeathBan;

public class PlayerListener implements Listener {
	private final EbeanServer database;

	private final long banDuration;

	public PlayerListener(EbeanServer database, long banDuration) {
		super();
		this.database = database;
		this.banDuration = banDuration;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final long now = System.currentTimeMillis();
		final long start = now - banDuration;

		List<DeathBan> bans = database.find(DeathBan.class).where()
				.eq("player", event.getPlayer().getName()).gt("banTime", start)
				.findList();

		if (!bans.isEmpty()) {
			DeathBan ban = bans.get(0);

			final long timeRemaining = ban.getBanTime() + banDuration - now;

			StringBuilder time = new StringBuilder();
			time.append("Because you died, you still have ");

			formatTime(timeRemaining, time);

			time.append(" left until you can play again");

			event.getPlayer().kickPlayer(time.toString());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityHeal(EntityRegainHealthEvent event) {
		if (event.isCancelled())
			return;

		switch (event.getRegainReason()) {
		case SATIATED:
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		StringBuilder time = new StringBuilder();
		time.append("You died, therefore you have been banned for ");

		formatTime(banDuration, time);

		event.getEntity().kickPlayer(time.toString());

		List<DeathBan> bans = database.find(DeathBan.class).where()
				.eq("player", event.getEntity().getName()).findList();

		database.delete(bans);

		DeathBan db = new DeathBan();
		db.setBanTime(System.currentTimeMillis());
		db.setPlayerName(event.getEntity().getName());

		database.save(db);
	}

	private void formatTime(final long timeRemaining, StringBuilder time) {
		final long secondsLeft = timeRemaining / 1000;
		final long minutesLeft = secondsLeft / 60;
		final long hoursLeft = minutesLeft / 60;
		final long daysLeft = hoursLeft / 24;

		if (daysLeft > 0) {
			time.append(daysLeft);
			time.append(" days ");
		} else if (hoursLeft > 0) {
			time.append(hoursLeft);
			time.append(" hours ");
		} else if (minutesLeft > 0) {
			time.append(minutesLeft);
			time.append(" minutes ");
		} else {
			time.append(secondsLeft);
			time.append(" seconds ");
		}
	}
}
