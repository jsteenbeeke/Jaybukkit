package com.jeroensteenbeeke.bk.ville.listeners;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerListener;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.VilleLocations;

public class FluidListener extends PlayerListener {
	private final VilleLocations locations;

	public FluidListener(Ville ville) {
		locations = ville.getLocations();
	}

	@Override
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		super.onPlayerBucketEmpty(event);

		if (event.isCancelled())
			return;

		if (!locations.hasBuilderPermission(event.getPlayer(), event
				.getBlockClicked().getLocation())) {
			event.setCancelled(true);
			Messages.send(event.getPlayer(),
					"&cYou do not have permission to build here");
		} else {
			Material m = event.getBucket();

			switch (m) {
			case WATER_BUCKET:
			case LAVA_BUCKET:
				if (!locations.isBuilderAt(event.getPlayer(), event
						.getBlockClicked().getLocation())) {
					event.setCancelled(true);
					Messages.send(
							event.getPlayer(),
							String.format(
									"&cPlacement of fluids is restricted in claimed areas",
									m.toString()));
				}
			default:
				break;
			}
		}

	}
}
