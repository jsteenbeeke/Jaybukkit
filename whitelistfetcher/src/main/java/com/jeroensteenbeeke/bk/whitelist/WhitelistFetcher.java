package com.jeroensteenbeeke.bk.whitelist;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class WhitelistFetcher extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	final static String DEFAULT_URL = "ENTER URL HERE";

	@Override
	public void onEnable() {
		logger.info("Enabled whitelistfetcher plugin");

		String url = getConfig().getString("url", DEFAULT_URL);
		saveConfig();

		if (!DEFAULT_URL.equals(url)) {
			FetcherTask ft = new FetcherTask(this, url);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, ft,
					60L * 20L, 3600L * 20L);

		} else {
			logger.severe("Invalid URL for whitelist fetching, will ignore!");
		}

	}

	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
	}
}
