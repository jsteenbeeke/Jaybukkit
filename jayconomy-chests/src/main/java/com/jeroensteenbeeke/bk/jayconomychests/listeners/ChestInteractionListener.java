package com.jeroensteenbeeke.bk.jayconomychests.listeners;

import java.util.List;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.bk.basics.listeners.ContainerTransactionListener;
import com.jeroensteenbeeke.bk.basics.util.BlockUtil;
import com.jeroensteenbeeke.bk.jayconomychests.ChestFilter;
import com.jeroensteenbeeke.bk.jayconomychests.JayconomyChests;

public class ChestInteractionListener extends ContainerTransactionListener {
	private final JayconomyChests chestsPlugin;

	public ChestInteractionListener(JayconomyChests chestsPlugin) {
		super(InventoryType.CHEST);
		this.chestsPlugin = chestsPlugin;
	}

	@Override
	protected boolean isMatch(InventoryClickEvent event) {
		InventoryHolder holder = event.getInventory().getHolder();

		if (holder instanceof Chest) {
			Chest chest = (Chest) holder;

			List<Sign> signs = Lists.newArrayList(Iterables.filter(BlockUtil
					.getChestSigns(chest), new ChestFilter(chestsPlugin)));

			return signs.size() == 1;

		}

		return false;
	}

}
