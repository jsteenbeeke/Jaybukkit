package com.jeroensteenbeeke.bk.playerbasics.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.playerbasics.PlayerBasics;
import com.jeroensteenbeeke.bk.playerbasics.util.PlayerUtil;

public class LoginListener extends PlayerListener {
	private final String motd;

	private final String servername;

	@SuppressWarnings("deprecation")
	public LoginListener(PlayerBasics plugin) {
		this.motd = plugin.getConfiguration().getString("motd", "");
		this.servername = plugin.getConfiguration().getString("servername", "");
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (player.hasPermission(PlayerBasics.PERMISSION_MOTD)) {

			StringBuilder name = new StringBuilder();
			name.append("&fWelcome to ");
			if (servername != null && !servername.isEmpty()) {
				name.append("&e");
				name.append(servername);
				name.append("&f");
			} else {
				name.append("the server");
			}
			name.append("!");

			Messages.send(player, name.toString());
			StringBuilder online = new StringBuilder();
			online.append("&fThe following players are online: [");
			online.append(PlayerUtil.compilePlayerList(player.getServer()));
			online.append("&f]");

			Messages.send(player, online.toString());

			if (motd != null && !motd.isEmpty()) {
				Messages.send(player, motd);
			}
		}

	}

}
