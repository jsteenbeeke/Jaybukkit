package com.jeroensteenbeeke.bukkit.killzone.handlers;

import java.util.logging.Logger;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.jeroensteenbeeke.bukkit.killzone.ZoneTracker;

public class KillZoneHandler implements Listener {
	private final ZoneTracker tracker;

	private Logger logger = Logger.getLogger("Minecraft");

	public KillZoneHandler(ZoneTracker tracker) {
		super();
		this.tracker = tracker;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMonsterSpawn(CreatureSpawnEvent event) {
		if (event.isCancelled())
			return;

		// Ignore boss mobs
		if (event.getEntityType() == EntityType.WITHER
				|| event.getEntityType() == EntityType.ENDER_DRAGON)
			return;

		if (tracker.isKillZone(event.getLocation())) {
			event.getEntity().damage(100000);
		}
	}

}
