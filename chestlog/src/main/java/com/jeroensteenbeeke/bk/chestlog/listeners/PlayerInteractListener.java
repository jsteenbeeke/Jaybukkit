/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.chestlog.listeners;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avaje.ebean.EbeanServer;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestData;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLocation;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLog;

public class PlayerInteractListener implements Listener {
	private final EbeanServer database;

	public PlayerInteractListener(EbeanServer database) {
		this.database = database;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
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
