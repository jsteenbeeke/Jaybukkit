package com.jeroensteenbeeke.bk.jaycore.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebean.EbeanServer;
import com.jeroensteenbeeke.bk.jaycore.entities.DeathBan;
import com.jeroensteenbeeke.bk.jaycore.entities.TorchGiven;

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
				.eq("playerName", event.getPlayer().getName())
				.gt("banTime", start).findList();

		if (!bans.isEmpty()) {
			DeathBan ban = bans.get(0);

			final long timeRemaining = ban.getBanTime() + banDuration - now;

			StringBuilder time = new StringBuilder();
			time.append("Because you died, you still have ");

			formatTime(timeRemaining, time);

			time.append(" left until you can play again");

			event.getPlayer().kickPlayer(time.toString());

			return;
		}

		List<TorchGiven> givens = database.find(TorchGiven.class).where()
				.eq("playerName", event.getPlayer().getName()).findList();

		if (givens.isEmpty()) {
			event.getPlayer().getInventory()
					.addItem(new ItemStack(Material.TORCH));

			TorchGiven given = new TorchGiven();
			given.setPlayername(event.getPlayer().getName());

			database.save(given);
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
	public void onPlayerDeath(PlayerRespawnEvent event) {
		if (banDuration > 0) {
			StringBuilder time = new StringBuilder();
			time.append("You died, therefore you have been banned for ");

			formatTime(banDuration, time);

			event.getPlayer().kickPlayer(time.toString());

			List<DeathBan> bans = database.find(DeathBan.class).where()
					.eq("playerName", event.getPlayer().getName()).findList();

			database.delete(bans);

			DeathBan db = new DeathBan();
			db.setBanTime(System.currentTimeMillis());
			db.setPlayerName(event.getPlayer().getName());

			database.save(db);
		}
	}

	private void formatTime(final long timeRemaining, StringBuilder time) {
		final long secondsLeft = timeRemaining / 1000;
		final long minutesLeft = secondsLeft / 60;
		final long hoursLeft = minutesLeft / 60;
		final long daysLeft = hoursLeft / 24;

		if (daysLeft > 0) {
			time.append(daysLeft);
			time.append(" days");
			long ehours = hoursLeft - (24 * daysLeft);
			if (ehours > 0) {
				time.append(" and ");
				time.append(ehours);
				time.append(" hours");
			}

		} else if (hoursLeft > 0) {
			time.append(hoursLeft);
			time.append(" hours");
			long emins = minutesLeft - (60 * hoursLeft);
			if (emins > 0) {
				time.append(" and ");
				time.append(emins);
				time.append(" minutes");
			}

		} else if (minutesLeft > 0) {
			time.append(minutesLeft);
			time.append(" minutes");
		} else {
			time.append(secondsLeft);
			time.append(" seconds");
		}
	}
}
