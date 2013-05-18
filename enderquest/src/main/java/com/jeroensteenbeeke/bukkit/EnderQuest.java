package com.jeroensteenbeeke.bukkit;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class EnderQuest extends JSPlugin {
	public static final String PERMISSION = "enderquest.embark";
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled enderquest plugin");

		addCommandHandler(new EnderQuestCommand(this));
		addListener(new PlayerDeathListener(this));
	}

	@Override
	public void onDisable() {

	}
}
