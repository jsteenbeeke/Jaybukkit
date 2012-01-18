package com.jeroensteenbeeke.bk.jayop;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FreezeTask implements Runnable {
	private JayOp jayOp;

	public FreezeTask(JayOp jayOp) {
		this.jayOp = jayOp;
	}

	@Override
	public void run() {
		for (String player : jayOp.getFrozen()) {
			Player p = jayOp.getServer().getPlayerExact(player);

			if (p != null) {
				p.setVelocity(new Vector(0, 0, 0));
			}
		}

	}

}
