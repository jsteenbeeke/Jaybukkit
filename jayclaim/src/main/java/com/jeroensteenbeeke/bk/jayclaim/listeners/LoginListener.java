package com.jeroensteenbeeke.bk.jayclaim.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayclaim.JayClaim;
import com.jeroensteenbeeke.bk.jayclaim.entities.Claim;

public class LoginListener implements Listener {
	private final JayClaim jayclaim;

	public LoginListener(JayClaim jayclaim) {
		this.jayclaim = jayclaim;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (jayclaim.getDatabase().find(Claim.class).findRowCount() == 0) {
			int free = jayclaim.getFreeclaims();

			Messages.send(event.getPlayer(),
					"You have not yet claimed any pieces of land.");

			if (free == 1) {
				Messages.send(event.getPlayer(),
						"The first piece can be claimed for free");
			} else {
				Messages.send(event.getPlayer(), String.format(
						"The first %d pieces can be claimed for free", free));
			}

			Messages.send(event.getPlayer(),
					"Type &e/jc claim&f to claim a piece of land");

		}
	}

}
