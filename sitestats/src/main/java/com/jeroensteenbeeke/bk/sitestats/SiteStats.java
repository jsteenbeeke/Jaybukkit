package com.jeroensteenbeeke.bk.sitestats;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class SiteStats extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onLoad() {
		String target = getConfig().getString("targetUrl",
				"https://www.daeshara.com/updateUserList.php");
		getConfig().set("targetUrl", target);
		String key = getConfig().getString("key", "");
		getConfig().set("key", key);

		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled sitestats plugin");

		String key = getConfig().getString("key");

		if (key == null || key.isEmpty()) {
			logger.severe("Please edit the sitestats config.yml file and enter your key");
		} else {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new UpdateSiteTask(this), 20 * 60 * 1, 20 * 60 * 3);
		}
	}

	@Override
	public void onDisable() {

	}
}
