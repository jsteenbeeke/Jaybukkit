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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.jeroensteenbeeke.bk.premium.PlayerType;
import com.jeroensteenbeeke.bk.premium.PremiumMembers;

public class PremiumPlayerListener extends PlayerListener {
	private static final Logger log = Logger.getLogger("Minecraft");

	private final PremiumMembers plugin;

	private final Map<PlayerType, Integer> slots;

	public PremiumPlayerListener(PremiumMembers plugin, int premium, int guest) {
		this.plugin = plugin;
		Map<PlayerType, Integer> base = new EnumMap<PlayerType, Integer>(
				PlayerType.class);

		int max = plugin.getServer().getMaxPlayers();
		int regular = max - premium - guest;

		log.info(String.format("%d guest slots", guest));
		log.info(String.format("%d regular slots", regular));
		log.info(String.format("%d premium slots", premium));

		base.put(PlayerType.GUEST, guest);
		base.put(PlayerType.REGULAR, regular);
		base.put(PlayerType.PREMIUM, premium);

		this.slots = Collections.unmodifiableMap(base);
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer() != null) {
			PlayerType orig = getPlayerType(event.getPlayer());

			Map<PlayerType, Integer> available = new EnumMap<PlayerType, Integer>(
					slots);

			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if (!player.equals(event.getPlayer())) {
					PlayerType t = getPlayerType(player);

					available.put(t, available.get(t) - 1);
				}
			}

			PlayerType type = orig;

			while (type != null) {
				Integer slots = available.get(type);

				if (slots >= 1) {
					return;
				}

				type = type.getLowerType();
			}

			event.getPlayer().kickPlayer(orig.getKickMessage());
		}
	}

	private PlayerType getPlayerType(Player player) {
		PlayerType highestQualifyingType = PlayerType.GUEST;

		if (player.hasPermission(PremiumMembers.PERMISSION_PREMIUM)) {
			highestQualifyingType = PlayerType.PREMIUM;
		} else if (player.hasPermission(PremiumMembers.PERMISSION_REGULAR)) {
			highestQualifyingType = PlayerType.REGULAR;
		}

		return highestQualifyingType;
	}

}
