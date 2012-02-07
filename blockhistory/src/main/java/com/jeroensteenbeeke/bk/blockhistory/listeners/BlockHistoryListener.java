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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import com.jeroensteenbeeke.bk.blockhistory.BlockHistory;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange.BlockChangeType;

public class BlockHistoryListener implements Listener {
	private final BlockHistory history;

	public BlockHistoryListener(BlockHistory history) {
		this.history = history;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Block block = event.getBlock();

		history.removeBlock(block, event.getPlayer().getName(),
				BlockChangeType.DESTROYED);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFade(BlockFadeEvent event) {
		if (event.isCancelled())
			return;

		history.removeBlock(event.getBlock(), null, BlockChangeType.FADED);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockForm(BlockFormEvent event) {
		if (event.isCancelled())
			return;

		history.addBlock(event.getBlock(), null, BlockChangeType.FORMED);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockSpread(BlockSpreadEvent event) {
		if (event.isCancelled())
			return;

		history.addBlock(event.getBlock(), null, BlockChangeType.SPREAD);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		history.addBlock(event.getBlock(), event.getPlayer().getName(),
				BlockChangeType.PLACED);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (event.isCancelled())
			return;

		history.removeBlock(event.getBlock(), null, BlockChangeType.DECAYED);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		if (event.isCancelled())
			return;

		for (Block b : event.getBlocks()) {
			history.addBlock(b, null, BlockChangeType.PISTONED);
		}
	}

}
