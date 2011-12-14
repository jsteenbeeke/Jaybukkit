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
package com.jeroensteenbeeke.bk.playerbasics.util;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public final class PlayerUtil {

	private PlayerUtil() {
	}

	public static String compilePlayerList(Server server) {
		StringBuilder builder = new StringBuilder();

		int i = 0;

		for (Player player : server.getOnlinePlayers()) {
			if (i++ > 0) {
				builder.append("&f, ");
			}
			builder.append(getColoredPlayerName(player));
		}

		return builder.toString();
	}

	private static String getColoredPlayerName(Player player) {
		for (ChatColor color : ChatColor.values()) {
			if (player
					.hasPermission("colornames." + color.name().toLowerCase())) {
				return color.toString() + player.getDisplayName();
			}
		}

		return player.getName();
	}
}
