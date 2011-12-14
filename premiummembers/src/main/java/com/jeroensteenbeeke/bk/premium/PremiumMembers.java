package com.jeroensteenbeeke.bk.premium;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.premium.listeners.PremiumPlayerListener;

public class PremiumMembers extends JSPlugin {
	public static final String PERMISSION_PREMIUM = "premiummembers.premium";
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled premium members plugin");

		int slots = getConfig().getInt("nonpremiumslots", 0);

		saveConfig();

		addListener(Type.PLAYER_JOIN, new PremiumPlayerListener(this, slots),
				Priority.Lowest);
	}

	@Override
	public void onDisable() {

	}
}
