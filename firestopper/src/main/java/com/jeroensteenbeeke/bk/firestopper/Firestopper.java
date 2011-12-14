package com.jeroensteenbeeke.bk.firestopper;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.firestopper.listeners.FirestopperListener;

public class Firestopper extends JSPlugin {
	public static final String PERMISSION_START_FIRE = "firestarter.fire";
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	@SuppressWarnings("deprecation")
	public void onEnable() {
		logger.info("Enabled Firestopper plugin");

		int burnIfUnauthorized = getConfiguration().getInt(
				"burnIfUnauthorized", 0);

		addListener(Type.BLOCK_IGNITE, new FirestopperListener(
				burnIfUnauthorized), Priority.Lowest);
	}

	@Override
	public void onDisable() {
		logger.info("Disabled Firestopper plugin");
	}
}
