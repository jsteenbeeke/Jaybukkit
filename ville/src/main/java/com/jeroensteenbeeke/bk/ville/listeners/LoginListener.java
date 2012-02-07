package com.jeroensteenbeeke.bk.ville.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;

public class LoginListener implements Listener {
	private Ville ville;

	public LoginListener(Ville ville) {
		this.ville = ville;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!ville.getLocations().isApprovedBuilder(event.getPlayer())) {
			Messages.send(event.getPlayer(),
					"&cYou do not yet have universal build permissions");
			Messages.send(event.getPlayer(),
					"&cType &e/ville approve me&c to enable them");
		}
	}
}
