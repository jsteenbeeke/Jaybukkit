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
package com.jeroensteenbeeke.bk.jayconomy.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;

public class SignExplodeListener implements Listener {
	private final Jayconomy plugin;

	public SignExplodeListener(Jayconomy plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;

		int affectedCount = event.blockList().size();

		if (affectedCount == 0)
			return;

		for (Block b : event.blockList()) {
			if (b instanceof Sign) {
				if (plugin.getDatabase().createQuery(JayconomySign.class)
						.where().eq("x", b.getX()).eq("y", b.getY())
						.eq("z", b.getZ()).eq("world", b.getWorld().getName())
						.findRowCount() > 0) {
					event.setCancelled(true);
					return;
				}
			}
		}

	}
}
