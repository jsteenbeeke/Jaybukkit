package com.jeroensteenbeeke.bk.waypoint.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.jeroensteenbeeke.bk.waypoint.Waypoints;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class MovementListener extends PlayerListener implements Listener {
	private Waypoints waypoints;

	public MovementListener(Waypoints waypoints) {
		this.waypoints = waypoints;
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Waypoint to = waypoints.getWaypoint(event.getTo());
		Waypoint from = waypoints.getWaypoint(event.getFrom());

		if (to != null && from == null) {
			event.getPlayer().sendMessage(
					"Entered waypoint \u00A7a" + to.getName() + "\u00A7f");
		} else if (to == null && from != null) {
			event.getPlayer().sendMessage(
					"Exited waypoint \u00A7a" + from.getName() + "\u00A7f");
		}
	}
}
