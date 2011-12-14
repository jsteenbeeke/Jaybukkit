package com.jeroensteenbeeke.bk.waypoint.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.Waypoints;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class ProtectionEntityListener extends EntityListener {
	private final Waypoints waypoints;

	private WaypointPlugin plugin;

	public ProtectionEntityListener(WaypointPlugin plugin, Waypoints waypoints) {
		super();
		this.waypoints = waypoints;
		this.plugin = plugin;
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;

		for (Block block : event.blockList()) {
			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();

			if (waypoints.isWaypoint(x, y, z)) {
				Entity entity = event.getEntity();
				World world = entity.getWorld();

				Waypoint waypoint = waypoints.getWaypoint(new Location(world,
						x, y, z));

				event.setCancelled(true);
				plugin.getServer().broadcast(
						"Explosion prevented near waypoint \u00A7a"
								+ waypoint.getName()
								+ "\u00A7f in world \u00A7e" + world.getName()
								+ "\u00A7f", WaypointPlugin.CREATE_PERMISSION);
				return;
			}
		}
	}

}
