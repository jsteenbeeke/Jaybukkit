package com.jeroensteenbeeke.bk.jayconomy.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;

public class SignExplodeListener extends EntityListener implements Listener {
	private final Jayconomy plugin;

	public SignExplodeListener(Jayconomy plugin) {
		this.plugin = plugin;
	}

	@Override
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
