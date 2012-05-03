package com.jeroensteenbeeke.bk.timefreeze;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.World;

public class FreezeTask implements Runnable {
	private final Server server;

	private final Map<UUID, Long> times;

	public FreezeTask(Server server, Map<UUID, Long> times) {
		this.server = server;
		this.times = times;
	}

	@Override
	public void run() {
		for (Entry<UUID, Long> e : times.entrySet()) {
			World world = server.getWorld(e.getKey());

			world.setTime(e.getValue());
		}

	}

}
