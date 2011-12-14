package com.jeroensteenbeeke.bk.chestlog.listeners;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.avaje.ebean.EbeanServer;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestData;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLocation;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLog;

public class PlayerInteractListener extends PlayerListener {
	private final EbeanServer database;

	public PlayerInteractListener(EbeanServer database) {
		this.database = database;
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();

			ChestLocation location = getChestLocation(block.getLocation());
			if (location != null) {
				ChestData chest = location.getChest();

				ChestLog log = new ChestLog();
				log.setChest(chest);
				log.setDate(new Date());
				log.setMessage("Opened chest");
				log.setPlayer(event.getPlayer().getName());

				database.save(log);
			}
		}

	}

	private ChestLocation getChestLocation(Location loc) {
		return database.createQuery(ChestLocation.class).where()
				.eq("x", loc.getBlockX()).eq("y", loc.getBlockY())
				.eq("z", loc.getBlockZ()).eq("world", loc.getWorld().getName())
				.findUnique();
	}
}
