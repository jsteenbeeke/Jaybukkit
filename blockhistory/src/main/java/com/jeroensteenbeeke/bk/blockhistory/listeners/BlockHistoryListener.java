package com.jeroensteenbeeke.bk.blockhistory.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import com.jeroensteenbeeke.bk.blockhistory.BlockHistory;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange.BlockChangeType;

public class BlockHistoryListener extends BlockListener {
	private final BlockHistory history;

	public BlockHistoryListener(BlockHistory history) {
		this.history = history;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Block block = event.getBlock();

		history.removeBlock(block, event.getPlayer().getName(),
				BlockChangeType.DESTROYED);
	}

	@Override
	public void onBlockFade(BlockFadeEvent event) {
		if (event.isCancelled())
			return;

		history.removeBlock(event.getBlock(), null, BlockChangeType.FADED);
	}

	@Override
	public void onBlockForm(BlockFormEvent event) {
		if (event.isCancelled())
			return;

		history.addBlock(event.getBlock(), null, BlockChangeType.FORMED);
	}

	@Override
	public void onBlockSpread(BlockSpreadEvent event) {
		if (event.isCancelled())
			return;

		history.addBlock(event.getBlock(), null, BlockChangeType.SPREAD);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		history.addBlock(event.getBlock(), event.getPlayer().getName(),
				BlockChangeType.PLACED);
	}

	@Override
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (event.isCancelled())
			return;

		history.removeBlock(event.getBlock(), null, BlockChangeType.DECAYED);
	}

	@Override
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		if (event.isCancelled())
			return;

		for (Block b : event.getBlocks()) {
			history.addBlock(b, null, BlockChangeType.PISTONED);
		}
	}

}
