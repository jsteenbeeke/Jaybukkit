package com.jeroensteenbeeke.bk.jaywire;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jaywire.listeners.JaywireBlockListener;
import com.jeroensteenbeeke.bk.jaywire.listeners.JaywirePlayerListener;

public class Jaywire extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final BlockFace[] AROUND = new BlockFace[] { BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	public static final String PERMISSION_PLACE_CLICK = "jaywire.place.click";

	public static final String PERMISSION_PLACE_TRIP = "jaywire.place.trip";

	public static final String HEADER_CLICK = "[RSOnClick]";

	public static final String HEADER_CLICK_FANCY = "[\u00A7eRSOnClick\u00A70]";

	public static final String HEADER_TRIP = "[RSOnWalk]";

	public static final String HEADER_TRIP_FANCY = "[\u00A7eRSOnWalk\u00A70]";

	@Override
	public void onEnable() {
		logger.info("Enabled jaywire plugin");

		addListener(new JaywirePlayerListener(this));

		addListener(new JaywireBlockListener());
	}

	@Override
	public void onDisable() {

	}

	public void powerBlockBelow(Block block) {
		Block below = block.getRelative(BlockFace.DOWN);
		if (below.getType() == Material.REDSTONE_WIRE) {
			below.setData((byte) 0xE);
			logger.info("TEH POWRE!1");
		} else {
			logger.info(String.format(
					"No powerable block underneath (%d, %d, %d)", block.getX(),
					block.getY(), block.getZ()));
		}

	}
}
