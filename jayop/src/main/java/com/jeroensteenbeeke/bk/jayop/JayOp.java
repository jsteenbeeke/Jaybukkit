package com.jeroensteenbeeke.bk.jayop;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jayop.commands.BanCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.ClearInventoryCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.GiveItemCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.KickCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.SuspendCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportOthersCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportToMeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TimeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.UnbanCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.UnsuspendCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.WeatherCommandHandler;
import com.jeroensteenbeeke.bk.jayop.entities.Suspension;
import com.jeroensteenbeeke.bk.jayop.listeners.SuspendedPlayerListener;

public class JayOp extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final String PERMISSION_ENVIRONMENT = "jayop.environment";

	public static final String PERMISSION_LOCATIONAL = "jayop.location";

	public static final String PERMISSION_ENFORCEMENT = "jayop.enforcement";

	public static final String PERMISSION_INVENTORY = "jayop.inventory";

	@Override
	public void onEnable() {
		logger.info("Enabled JayOp plugin");

		setupDatabase();

		addListener(Type.PLAYER_LOGIN, new SuspendedPlayerListener(this),
				Priority.Highest);

		addCommandHandler(new WeatherCommandHandler());
		addCommandHandler(new TeleportCommandHandler(this));
		addCommandHandler(new TeleportToMeCommandHandler());
		addCommandHandler(new TeleportOthersCommandHandler(this));
		addCommandHandler(new TimeCommandHandler());
		addCommandHandler(new KickCommandHandler());
		addCommandHandler(new SuspendCommandHandler(this));
		addCommandHandler(new BanCommandHandler());
		addCommandHandler(new UnbanCommandHandler(this));
		addCommandHandler(new UnsuspendCommandHandler(this));
		addCommandHandler(new GiveItemCommandHandler());
		addCommandHandler(new ClearInventoryCommandHandler());
	}

	private void setupDatabase() {
		try {
			getDatabase().find(Suspension.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Waypoint database");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(Suspension.class);

		return classes;
	}

	@Override
	public void onDisable() {

	}
}
