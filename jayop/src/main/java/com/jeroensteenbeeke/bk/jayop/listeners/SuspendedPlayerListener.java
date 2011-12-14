/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
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
