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
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import com.jeroensteenbeeke.bk.blockhistory.BlockHistory;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange.BlockChangeType;

public class BlockHistoryEntityListener extends EntityListener {
	private final BlockHistory history;

	public BlockHistoryEntityListener(BlockHistory history) {
		this.history = history;
	}

	@Override
	public void onEndermanPickup(EndermanPickupEvent event) {
		if (event.isCancelled())
			return;

		Block block = event.getBlock();

		history.removeBlock(block, null, BlockChangeType.ENDERMAN_REMOVE);
	}

	@Override
	public void onEndermanPlace(EndermanPlaceEvent event) {
		if (event.isCancelled())
			return;

		Block replaced = event.getLocation().getBlock();

		history.addBlock(replaced, null, BlockChangeType.ENDERMAN_PLACE);
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;

		for (Block b : event.blockList()) {
			history.removeBlock(b, null, BlockChangeType.EXPLODED);
		}

	}

}
