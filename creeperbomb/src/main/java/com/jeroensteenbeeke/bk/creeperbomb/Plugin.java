package com.jeroensteenbeeke.bk.creeperbomb;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.creeperbomb.commands.CreeperBombHandler;

public class Plugin extends JSPlugin {
	public static final String PERMISSION_NAME = "creeperbomb.use";
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled creeperbomb plugin");

		addCommandHandler(new CreeperBombHandler(this));
	}

	@Override
	public void onDisable() {

	}
}
