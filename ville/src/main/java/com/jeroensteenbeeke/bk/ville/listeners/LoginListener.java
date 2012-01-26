package com.jeroensteenbeeke.bk.ville.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;

public class LoginListener extends PlayerListener {
	private Ville ville;

	public LoginListener(Ville ville) {
		this.ville = ville;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		super.onPlayerJoin(event);

		if (!ville.getLocations().isApprovedBuilder(event.getPlayer())) {
			Messages.send(event.getPlayer(),
					"&cYou do not yet have universal build permissions");
			Messages.send(event.getPlayer(),
					"&cType &e/ville approve me&c to enable them");
		}
	}
}
