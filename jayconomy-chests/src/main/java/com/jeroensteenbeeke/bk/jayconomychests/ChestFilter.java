package com.jeroensteenbeeke.bk.jayconomychests;

import org.bukkit.block.Sign;

import com.google.common.base.Predicate;

public class ChestFilter implements Predicate<Sign> {
	private final JayconomyChests chestsPlugin;

	public ChestFilter(JayconomyChests chestsPlugin) {
		this.chestsPlugin = chestsPlugin;
	}

	@Override
	public boolean apply(Sign sign) {
		return chestsPlugin.isJayconomyChestSign(sign);
	}

}
