package com.jeroensteenbeeke.bk.jaylock.jobs;

import java.util.List;

import org.bukkit.block.Block;

import com.jeroensteenbeeke.bk.jaylock.Jaylock;
import com.jeroensteenbeeke.bk.jaylock.entities.ForbiddenLocation;

public class SetOwnerTask implements Runnable {
	private final Jaylock jaylock;

	private final String owner;

	private final List<Block> targetBlocks;

	public SetOwnerTask(Jaylock jaylock, String owner, List<Block> targetBlocks) {
		this.jaylock = jaylock;
		this.owner = owner;
		this.targetBlocks = targetBlocks;
	}

	@Override
	public void run() {
		jaylock.getDatabase().beginTransaction();
		for (Block b : targetBlocks) {
			ForbiddenLocation fl = jaylock.getForbiddenLocation(b);

			if (fl != null) {
				fl.setOwner(owner);
				jaylock.getDatabase().update(fl);
			}
		}
		jaylock.getDatabase().commitTransaction();

	}

}
