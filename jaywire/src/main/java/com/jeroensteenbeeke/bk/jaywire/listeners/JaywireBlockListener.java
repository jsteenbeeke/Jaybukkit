package com.jeroensteenbeeke.bk.jaywire.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

import com.jeroensteenbeeke.bk.jaywire.Jaywire;

public class JaywireBlockListener extends BlockListener {
	@Override
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled())
			return;

		String firstLine = event.getLine(0);

		if (Jaywire.HEADER_CLICK.equals(firstLine)) {
			if (!event.getPlayer()
					.hasPermission(Jaywire.PERMISSION_PLACE_CLICK)) {
				event.setCancelled(true);
			} else {
				event.setLine(1, event.getPlayer().getName());
				event.setLine(2, "");
				event.setLine(3, "");
			}
		} else if (Jaywire.HEADER_TRIP.equals(firstLine)) {
			if (!event.getPlayer().hasPermission(Jaywire.PERMISSION_PLACE_TRIP)) {
				event.setCancelled(true);
			} else {
				event.setLine(1, event.getPlayer().getName());
				event.setLine(2, "");
				event.setLine(3, "");
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Block broken = event.getBlock();
		if (!isOwnerOrNotWired(broken, event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		super.onBlockDamage(event);

		if (event.isCancelled())
			return;

		Block broken = event.getBlock();

		if (!isOwnerOrNotWired(broken, event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	private boolean isOwnerOrNotWired(Block block, Player player) {
		for (BlockFace rel : Jaywire.AROUND) {
			Block next = block.getRelative(rel);
			if (next.getType() == Material.WALL_SIGN) {
				if (next.getState() instanceof Sign) {
					Sign s = (Sign) next.getState();
					if (Jaywire.HEADER_CLICK.equals(s.getLine(0))
							|| Jaywire.HEADER_TRIP.equals(s.getLine(0))) {
						return s.getLine(1).equals(player.getName());
					}
				}
			}
		}

		return true;
	}
}
