package com.jeroensteenbeeke.bk.colornames;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class ColorNames extends JSPlugin
{
	private Logger logger = Logger.getLogger("Minecraft");

	static final String PERMISSION_PREFIX = "colornames.";

	@Override
	public void onEnable()
	{
		logger.info("Enabled colornames plugin");

		addListener(Type.PLAYER_CHAT, new ChatColorListener(), Priority.Normal);
	}

	@Override
	public void onDisable()
	{

	}
}
