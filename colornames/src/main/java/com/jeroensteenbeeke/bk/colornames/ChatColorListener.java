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
package com.jeroensteenbeeke.bk.colornames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatColorListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
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
