package com.jeroensteenbeeke.bk.dampener;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class DampenExplosion extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled dampenexplosion plugin");

		ExplosionListener listener = new ExplosionListener();

		addListener(Type.ENTITY_EXPLODE, listener, Priority.Normal);
		addListener(Type.ENDERMAN_PICKUP, listener, Priority.Normal);
		addListener(Type.ENDERMAN_PLACE, listener, Priority.Normal);
	}

	@Override
	public void onDisable() {

	}
}
