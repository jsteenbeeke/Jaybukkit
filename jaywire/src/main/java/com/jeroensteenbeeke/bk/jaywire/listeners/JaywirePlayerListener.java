package com.jeroensteenbeeke.bk.jaywire.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.jeroensteenbeeke.bk.jaywire.Jaywire;

public class JaywirePlayerListener implements Listener {

	private final Jaywire plugin;

	public JaywirePlayerListener(Jaywire plugin) {
		super();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Block target = event.getTo().getBlock();
		Block standingOn = target.getRelative(BlockFace.DOWN);

		checkBlock(standingOn, Jaywire.HEADER_TRIP_FANCY);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clicked = event.getClickedBlock();

			checkBlock(clicked, Jaywire.HEADER_CLICK_FANCY);
		}
	}

	private void checkBlock(Block block, String expectedSignText) {
		for (BlockFace rel : Jaywire.AROUND) {
			Block next = block.getRelative(rel);
			if (next.getType() == Material.WALL_SIGN) {
				if (next.getState() instanceof Sign) {
					Sign s = (Sign) next.getState();
					if (expectedSignText.equals(s.getLine(0))) {
						plugin.powerBlockBelow(block);
					}
				}
			}
		}
	}

}
