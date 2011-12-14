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
