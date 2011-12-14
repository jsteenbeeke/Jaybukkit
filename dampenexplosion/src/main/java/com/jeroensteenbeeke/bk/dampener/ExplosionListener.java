package com.jeroensteenbeeke.bk.dampener;

import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class ExplosionListener extends EntityListener {
	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;

		event.setCancelled(true);
	}

	@Override
	public void onEndermanPickup(EndermanPickupEvent event) {
		if (event.isCancelled())
			return;

		event.setCancelled(true);
	}

	@Override
	public void onEndermanPlace(EndermanPlaceEvent event) {
		if (event.isCancelled())
			return;

		event.setCancelled(true);
	}
}
