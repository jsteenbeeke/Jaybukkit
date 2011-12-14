package com.jeroensteenbeeke.bk.jayop.listeners;

import java.util.List;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.jeroensteenbeeke.bk.jayop.JayOp;
import com.jeroensteenbeeke.bk.jayop.entities.Suspension;

public class SuspendedPlayerListener extends PlayerListener {
	private final JayOp plugin;

	public SuspendedPlayerListener(JayOp plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		List<Suspension> suspensions = plugin.getDatabase()
				.createQuery(Suspension.class).where()
				.eq("playerName", event.getPlayer().getName()).findList();

		for (Suspension suspension : suspensions) {
			if (suspension.getStart() + suspension.getDuration() > System
					.currentTimeMillis()) {
				long remainder = (suspension.getStart() + suspension
						.getDuration()) - System.currentTimeMillis();
				remainder = remainder / 1000;

				if (remainder > 60) {
					remainder = remainder / 60;
					if (remainder > 60) {
						remainder = remainder / 60;

						if (remainder > 24) {
							remainder = remainder / 24;
							event.disallow(Result.KICK_OTHER,
									"You are suspended for another "
											+ remainder + " days");
						} else {

							event.disallow(Result.KICK_OTHER,
									"You are suspended for another "
											+ remainder + " hours");
						}
					} else {
						event.disallow(Result.KICK_OTHER,
								"You are suspended for another " + remainder
										+ " minutes");
					}
				} else {
					event.disallow(Result.KICK_OTHER,
							"You are suspended for another " + remainder
									+ " seconds");
				}
			} else {
				plugin.getDatabase().delete(suspension);
			}
		}
	}
}
