package com.jeroensteenbeeke.bk.jaywire;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jaywire.listeners.JaywirePlayerListener;

public class Jaywire extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final BlockFace[] AROUND = new BlockFace[] { BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	public static final String PERMISSION_PLACE_CLICK = "jaywire.place.click";

	public static final String PERMISSION_PLACE_TRIP = "jaywire.place.trip";

	public static final String HEADER_CLICK = "[RSOnClick]";

	public static final String HEADER_TRIP = "[RSOnWalk]";

	@Override
	public void onEnable() {
		logger.info("Enabled jaywire plugin");

		JaywirePlayerListener pListener = new JaywirePlayerListener(this);

		addListener(Type.PLAYER_MOVE, pListener, Priority.Monitor);
		addListener(Type.PLAYER_INTERACT, pListener, Priority.Monitor);
	}

	@Override
	public void onDisable() {

	}

	public void powerBlockBelow(Block block) {
		Block below = block.getRelative(BlockFace.DOWN);
		if (below.getType() == Material.REDSTONE_WIRE) {
			below.setData((byte) 1, true);
		}

	}
}
