package com.jeroensteenbeeke.bk.endblockade;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class EndBlockade extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled endblockade plugin");

		addListener(new EndTeleportListener(getServer()));

	}

	@Override
	public void onDisable() {

	}
}
