package com.jeroensteenbeeke.bk.jaycore;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class JayCore extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled jaycore plugin");

	}

	@Override
	public void onDisable() {

	}
}
