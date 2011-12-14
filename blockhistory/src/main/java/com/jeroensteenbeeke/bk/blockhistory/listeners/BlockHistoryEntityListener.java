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
