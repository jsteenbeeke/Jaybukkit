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
package com.jeroensteenbeeke.bk.blockhistory.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.jeroensteenbeeke.bk.blockhistory.BlockHistory;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange.BlockChangeType;

public class BlockHistoryEntityListener implements Listener {
	private final BlockHistory history;

	public BlockHistoryEntityListener(BlockHistory history) {
		this.history = history;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEndermanPickupOrDrop(EntityChangeBlockEvent event) {
		if (event.isCancelled())
			return;

		Block block = event.getBlock();

		if (event.getEntityType() == EntityType.ENDERMAN
				|| event.getEntityType() == EntityType.ENDER_DRAGON)
			history.removeBlock(block, null, BlockChangeType.ENDERMAN_REMOVE);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;

		for (Block b : event.blockList()) {
			history.removeBlock(b, null, BlockChangeType.EXPLODED);
		}

	}

}
