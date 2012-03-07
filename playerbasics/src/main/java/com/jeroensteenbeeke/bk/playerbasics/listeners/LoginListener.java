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
package com.jeroensteenbeeke.bk.playerbasics.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.playerbasics.PlayerBasics;
import com.jeroensteenbeeke.bk.playerbasics.util.PlayerUtil;

public class LoginListener implements Listener {
	private final String motd;

	private final String servername;

	public LoginListener(PlayerBasics plugin) {
		this.motd = plugin.getConfig().getString("motd", "");
		this.servername = plugin.getConfig().getString("servername", "");
	}

	@EventHandler(priority = EventPriority.LOW)
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
