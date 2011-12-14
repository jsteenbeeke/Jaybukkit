package com.jeroensteenbeeke.bk.jaymail.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.jeroensteenbeeke.bk.jaymail.JaymailPlugin;

public class MailLoginListener extends PlayerListener {
	private JaymailPlugin plugin;

	public MailLoginListener(JaymailPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		plugin.viewMails(player);
	}
}
