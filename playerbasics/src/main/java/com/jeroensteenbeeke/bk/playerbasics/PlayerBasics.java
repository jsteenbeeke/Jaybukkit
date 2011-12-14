package com.jeroensteenbeeke.bk.playerbasics;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.playerbasics.commands.HelpHandler;
import com.jeroensteenbeeke.bk.playerbasics.commands.KillMeCommandHandler;
import com.jeroensteenbeeke.bk.playerbasics.commands.PlayerListHandler;
import com.jeroensteenbeeke.bk.playerbasics.commands.RulesHandler;
import com.jeroensteenbeeke.bk.playerbasics.listeners.LoginListener;

public class PlayerBasics extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final String PERMISSION_HELP = "playerbasics.help";
	public static final String PERMISSION_LIST = "playerbasics.list";
	public static final String PERMISSION_MOTD = "playerbasics.motd";
	public static final String PERMISSION_RULES = "playerbasics.rules";
	public static final String PERMISSION_KILL_ME = "playerbasics.killme";

	@Override
	public void onEnable() {
		logger.info("Enabled playerbasics plugin");

		addCommandHandler(new HelpHandler());
		addCommandHandler(new PlayerListHandler());
		addCommandHandler(new RulesHandler(this));
		addCommandHandler(new KillMeCommandHandler());
		addListener(Type.PLAYER_JOIN, new LoginListener(this), Priority.Low);
	}

	@Override
	public void onDisable() {

	}
}
