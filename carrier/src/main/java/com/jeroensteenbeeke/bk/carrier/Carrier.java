package com.jeroensteenbeeke.bk.carrier;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class Carrier extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	private static final class Defaults {
		public static final List<String> TRACKED_FILES = Lists
				.newArrayList("CHANGE ME");

		public static final String TARGET_URL = "CHANGE ME";

		public static final String USERNAME = "CHANGE ME";

		public static final String PASSWORD = "CHANGE ME";

		public static final Long INTERVAL = 20L * 60L * 60L;
	}

	@Override
	public void onLoad() {
		getConfig().addDefault("trackedFiles", Defaults.TRACKED_FILES);
		getConfig().addDefault("targetUrl", Defaults.TARGET_URL);
		getConfig().addDefault("interval", Defaults.INTERVAL);
		getConfig().addDefault("username", Defaults.USERNAME);
		getConfig().addDefault("password", Defaults.PASSWORD);

		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled carrier plugin");
		List<String> trackedFileNames = getConfig().getStringList(
				"trackedFiles");
		String targetUrl = getConfig().getString("targetUrl",
				Defaults.TARGET_URL);
		Long interval = getConfig().getLong("interval", Defaults.INTERVAL);
		String username = getConfig().getString("username", Defaults.USERNAME);
		String password = getConfig().getString("password", Defaults.PASSWORD);

		if (!Defaults.TARGET_URL.equals(targetUrl)
				&& !Defaults.USERNAME.equals(username)
				&& !Defaults.PASSWORD.equals(password)
				&& !Defaults.TRACKED_FILES.equals(trackedFileNames)) {
			getServer().getScheduler().scheduleAsyncRepeatingTask(
					this,
					new FileSyncer(targetUrl, username, password,
							trackedFileNames), 20L * 60L, interval);
		} else {
			logger.severe("Carrier plugin not set up properly. Not started");
		}

	}

	@Override
	public void onDisable() {

	}
}
