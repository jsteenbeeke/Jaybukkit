package com.jeroensteenbeeke.bk.jayop.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class ProtectionListener implements Listener {
	public static final String PROTECTION_LABEL = "[Unbreaking]";

	private final JayOp plugin;

	public ProtectionListener(JayOp plugin) {
		super();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignPlace(SignChangeEvent event) {
		if (event.isCancelled())
			return;

		if (PROTECTION_LABEL.equalsIgnoreCase(event.getLine(0))) {
			Player player = event.getPlayer();

			if (player.hasPermission(JayOp.PERMISSION_MODIFY_WORLD)) {
				Messages.send(player, "&aThis block is now unbreakable");
			} else {
				event.setCancelled(false);
				Messages.send(player,
						"&cYou do not have permission to make blocks unbreaking");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

	}
}
