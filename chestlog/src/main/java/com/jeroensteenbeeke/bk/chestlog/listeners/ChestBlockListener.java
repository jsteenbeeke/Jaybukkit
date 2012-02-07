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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.chestlog.ChestLogPlugin;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestData;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLocation;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLog;

public class ChestBlockListener implements Listener {
	private final EbeanServer database;

	public ChestBlockListener(EbeanServer database) {
		this.database = database;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		Block placed = event.getBlockPlaced();
		if (placed.getType() == Material.CHEST) {
			Location location = placed.getLocation();

			Transaction transaction = database.beginTransaction();

			List<Location> adjacent = getAdjacent(location);
			for (Location adj : adjacent) {
				ChestLocation cloc = getChestLocation(adj);
				if (cloc != null) {
					// Add to found location

					ChestData chest = cloc.getChest();

					ChestLocation curr = new ChestLocation();
					curr.setChest(chest);
					curr.setWorld(location.getWorld().getName());
					curr.setX(location.getBlockX());
					curr.setY(location.getBlockY());
					curr.setZ(location.getBlockZ());
					database.save(curr, transaction);

					ChestLog log = new ChestLog();
					log.setChest(chest);
					log.setMessage("Chest block placed");
					log.setPlayer(event.getPlayer().getName());
					log.setDate(new Date());
					database.save(log, transaction);

					transaction.commit();

					return;
				}
			}

			// No existing location, create new
			ChestData chest = new ChestData();
			chest.setOwner(event.getPlayer().getName());
			database.save(chest);

			ChestLocation curr = new ChestLocation();
			curr.setChest(chest);
			curr.setWorld(location.getWorld().getName());
			curr.setX(location.getBlockX());
			curr.setY(location.getBlockY());
			curr.setZ(location.getBlockZ());
			database.save(curr, transaction);

			ChestLog log = new ChestLog();
			log.setChest(chest);
			log.setMessage("Chest block placed");
			log.setPlayer(event.getPlayer().getName());
			log.setDate(new Date());
			database.save(log, transaction);

			transaction.commit();

		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		if (!isBreakAllowed(event.getPlayer(), event.getBlock())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou are not allowed to break this chest");
		} else {
			// Cleanup chest data
			cleanChestData(event.getBlock());
		}
	}

	private void cleanChestData(Block block) {
		ChestLocation cl = getChestLocation(block.getLocation());
		if (cl != null) {
			ChestData chest = cl.getChest();

			for (ChestLog log : chest.getLogs()) {
				database.delete(log);
			}

			for (ChestLocation loc : chest.getLocations()) {
				database.delete(loc);
			}

			database.delete(chest);

		}

	}

	private boolean isBreakAllowed(Player player, Block block) {
		if (player == null)
			return false;

		ChestLocation cl = getChestLocation(block.getLocation());
		if (cl != null) {
			ChestData chest = cl.getChest();

			if (player.hasPermission(ChestLogPlugin.PERMISSION_BREAK_CHEST)
					|| player.getName().equals(chest.getOwner())) {
				return true;
			}

			return false;
		}

		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.isCancelled())
			return;

		if (!isBreakAllowed(event.getPlayer(), event.getBlock())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou are not allowed to break this chest");
		}
	}

	private List<Location> getAdjacent(Location location) {
		List<Location> locations = new ArrayList<Location>(4);
		locations.add(new Location(location.getWorld(), location.getX() - 1,
				location.getY(), location.getZ()));
		locations.add(new Location(location.getWorld(), location.getX(),
				location.getY(), location.getZ() - 1));
		locations.add(new Location(location.getWorld(), location.getX() + 1,
				location.getY(), location.getZ()));
		locations.add(new Location(location.getWorld(), location.getX(),
				location.getY(), location.getZ() + 1));

		return locations;
	}

	private ChestLocation getChestLocation(Location loc) {
		return database.createQuery(ChestLocation.class).where()
				.eq("x", loc.getBlockX()).eq("y", loc.getBlockY())
				.eq("z", loc.getBlockZ()).eq("world", loc.getWorld().getName())
				.findUnique();
	}
}
