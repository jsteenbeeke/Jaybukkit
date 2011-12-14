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
