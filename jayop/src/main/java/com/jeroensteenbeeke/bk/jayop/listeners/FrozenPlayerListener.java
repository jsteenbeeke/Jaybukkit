package com.jeroensteenbeeke.bk.jayop.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.jeroensteenbeeke.bk.jayop.JayOp;

public class FrozenPlayerListener implements Listener {
	private final JayOp jayop;

	public FrozenPlayerListener(JayOp jayop) {
		this.jayop = jayop;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		if (jayop.getFrozen().contains(event.getPlayer().getName())) {
			Block from = event.getFrom().getBlock();
			Block to = event.getTo().getBlock();

			int dx = from.getX() - to.getX();
			int dy = from.getY() - to.getY();
			int dz = from.getZ() - to.getZ();

			if (dx != 0 || dy != 0 || dz != 0) {
				if (!event.getFrom().equals(event.getTo()))
					event.setCancelled(true);
			}
		}
	}
}
