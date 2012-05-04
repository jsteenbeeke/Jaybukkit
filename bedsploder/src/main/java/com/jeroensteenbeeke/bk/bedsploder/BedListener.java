package com.jeroensteenbeeke.bk.bedsploder;

import java.util.Set;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedListener implements Listener {
	private final Set<String> sploderWorlds;

	public BedListener(Set<String> sploderWorlds) {
		this.sploderWorlds = sploderWorlds;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBedClick(PlayerBedEnterEvent bedEvent) {
		if (bedEvent.isCancelled())
			return;

		Block bed = bedEvent.getBed();
		World world = bed.getWorld();

		if (!sploderWorlds.contains(world.getName()))
			return;

		bedEvent.setCancelled(true);

		bed.breakNaturally();

		world.createExplosion(bed.getLocation(), 4F);

	}

}
