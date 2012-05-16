package com.jeroensteenbeeke.bk.travelagent;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PortalEventListener implements Listener {
	private final Map<String, World> netherMappings;

	public PortalEventListener(Map<String, World> netherMappings) {
		super();
		this.netherMappings = netherMappings;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPortal(PlayerPortalEvent portalEvent) {
		if (portalEvent.isCancelled())
			return;

		if (portalEvent.getCause() == TeleportCause.NETHER_PORTAL) {
			final Location from = portalEvent.getFrom();
			final World fromWorld = from.getWorld();
			final String fromWorldName = fromWorld.getName();

			final Location to = portalEvent.getTo();
			final World toWorld = to.getWorld();

			if (netherMappings.containsKey(fromWorldName)) {
				World target = netherMappings.get(fromWorldName);

				if (!target.equals(toWorld)) {
					Location newTarget = new Location(target, to.getX(),
							to.getY(), to.getZ());
					portalEvent.setTo(newTarget);
				}

			}

		}
	}
}
