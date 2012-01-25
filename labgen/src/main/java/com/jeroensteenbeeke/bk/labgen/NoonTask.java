package com.jeroensteenbeeke.bk.labgen;

import org.bukkit.World;

public class NoonTask implements Runnable {
	private final LabGen labgen;

	public NoonTask(LabGen labgen) {
		super();
		this.labgen = labgen;
	}

	@Override
	public void run() {
		for (World w : labgen.getServer().getWorlds()) {
			w.setTime(6000L);
		}

	}

}
