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
package com.jeroensteenbeeke.bk.premium.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.jeroensteenbeeke.bk.premium.PremiumMembers;

public class PremiumPlayerListener extends PlayerListener {
	private final PremiumMembers plugin;

	private final int slots;

	public PremiumPlayerListener(PremiumMembers plugin, int slots) {
		this.plugin = plugin;
		this.slots = slots;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer() != null) {
			if (!event.getPlayer().hasPermission(PremiumMembers.PERMISSION_PREMIUM)) {

				int players = 0;
				for (Player player : plugin.getServer().getOnlinePlayers()) {
					if (!player.hasPermission(PremiumMembers.PERMISSION_PREMIUM)) {
						players++;
					}
				}

				if (players > slots) {
					event.getPlayer()
							.kickPlayer(
									"You are not a premium member, and no non-premium slots are available");
				}
			}
		}
	}
}
