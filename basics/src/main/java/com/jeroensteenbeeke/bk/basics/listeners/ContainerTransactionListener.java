package com.jeroensteenbeeke.bk.basics.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

import com.jeroensteenbeeke.bk.basics.util.Messages;

public abstract class ContainerTransactionListener implements Listener {

	private final InventoryType target;

	protected ContainerTransactionListener(InventoryType target) {
		this.target = target;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChestInteraction(InventoryClickEvent event) {
		InventoryView view = event.getView();

		if (view.getType() == target && isMatch(event)) {
			if (!event.isLeftClick() || !event.isShiftClick()) {
				event.setCancelled(true);

				if (event.getWhoClicked() instanceof Player)
					Messages.send(
							(Player) event.getWhoClicked(),
							String.format(
									"&cPlease use shift-lmb to interact with this %s",
									view.getType().getDefaultTitle()));

				return;
			}

		}
	}

	protected abstract boolean isMatch(InventoryClickEvent event);
}
