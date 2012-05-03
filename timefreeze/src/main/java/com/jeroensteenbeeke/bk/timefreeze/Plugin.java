package com.jeroensteenbeeke.bk.timefreeze;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class Plugin extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled timefreeze plugin");

	}

	@Override
	public void onDisable() {

	}
}
