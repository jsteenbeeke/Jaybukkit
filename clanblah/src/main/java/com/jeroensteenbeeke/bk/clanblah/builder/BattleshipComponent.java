package com.jeroensteenbeeke.bk.clanblah.builder;

import org.bukkit.block.Block;

public interface BattleshipComponent {
	void construct(Block battleshipLocation);

	void deconstruct(Block battleshipLocation);
}
