package com.jeroensteenbeeke.bk.spleefregen.listeners;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefBlockListener extends BlockListener {
	private final SpleefRegen plugin;

	public SpleefBlockListener(SpleefRegen plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Block b = event.getBlock();

		List<SpleefLocation> blocks = plugin.getDatabase()
				.createQuery(SpleefLocation.class).where().eq("x", b.getX())
				.eq("y", b.getY()).eq("z", b.getZ()).findList();
		for (SpleefLocation loc : blocks) {
			SpleefPoint point = loc.getPoint();
			if (point.isLocked()) {
				if (b.getWorld().getName().equals(point.getWorld())) {
					event.setCancelled(true);
					if (event.getPlayer() != null) {
						Messages.send(event.getPlayer(),
								"&cThis spleef area is locked");
					}
					return;
				}
			}
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		Block b = event.getBlock();

		List<SpleefLocation> blocks = plugin.getDatabase()
				.createQuery(SpleefLocation.class).where().eq("x", b.getX())
				.eq("y", b.getY()).eq("z", b.getZ()).findList();
		for (SpleefLocation loc : blocks) {
			SpleefPoint point = loc.getPoint();
			if (b.getWorld().getName().equals(point.getWorld())) {
				event.setCancelled(true);
				if (event.getPlayer() != null) {
					Messages.send(event.getPlayer(),
							"&cCannot manually replace spleef blocks");
				}
				return;
			}
		}

	}
}
