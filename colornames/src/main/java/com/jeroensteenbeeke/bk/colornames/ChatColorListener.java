package com.jeroensteenbeeke.bk.colornames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class ChatColorListener extends PlayerListener {
	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		for (ChatColor color : ChatColor.values()) {
			if (player.hasPermission(ColorNames.PERMISSION_PREFIX
					+ color.name().toLowerCase())) {
				format(event, color);
				return;
			}
		}
	}

	private void format(PlayerChatEvent event, ChatColor color) {
		event.setFormat("<" + color.toString() + "%1$s\u00A7f> %2$s");

	}
}
