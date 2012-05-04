package com.jeroensteenbeeke.bk.dropteleport;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class Plugin extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled dropteleport plugin");

	}

	@Override
	public void onDisable() {

	}
}
