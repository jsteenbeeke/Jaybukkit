package com.jeroensteenbeeke.bk.jayclaim.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayclaim.JayClaim;
import com.jeroensteenbeeke.bk.jayclaim.entities.Claim;

public class MovementListener implements Listener {
	private final JayClaim jayclaim;

	public MovementListener(JayClaim jayclaim) {
		super();
		this.jayclaim = jayclaim;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Claim from = jayclaim.getTracker().getClaimAt(event.getFrom());
		Claim to = jayclaim.getTracker().getClaimAt(event.getTo());

		if (from != null && to != null) {
			if (!from.equals(to)) {
				Messages.send(event.getPlayer(), to.getEnterMessage());
			}
		} else if (from != null) {
			Messages.send(event.getPlayer(), from.getExitMessage());
		} else if (to != null) {
			Messages.send(event.getPlayer(), to.getEnterMessage());
		}

	}
}
