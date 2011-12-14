package com.jeroensteenbeeke.bk.waypoint;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.waypoint.commands.WaypointCreateHandler;
import com.jeroensteenbeeke.bk.waypoint.commands.WaypointDestroyHandler;
import com.jeroensteenbeeke.bk.waypoint.commands.WaypointListHandler;
import com.jeroensteenbeeke.bk.waypoint.commands.WaypointUseHandler;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;
import com.jeroensteenbeeke.bk.waypoint.listeners.BlockProtectionListener;
import com.jeroensteenbeeke.bk.waypoint.listeners.MovementListener;
import com.jeroensteenbeeke.bk.waypoint.listeners.ProtectionEntityListener;

public class WaypointPlugin extends JSPlugin {
	public static final String CREATE_PERMISSION = "waypoint.create";

	public static final String USE_PERMISSION = "waypoint.use";

	private Logger logger = Logger.getLogger("Minecraft");

	private Waypoints waypoints;

	@Override
	public void onEnable() {
		logger.info("[Waypoint] Enabled Waypoint plugin");

		setupDatabase();

		waypoints = new Waypoints(this);

		addListener(Type.PLAYER_MOVE, new MovementListener(waypoints),
				Priority.Low);
		addListener(Type.BLOCK_BREAK, new BlockProtectionListener(waypoints),
				Priority.High);
		addListener(Type.BLOCK_DAMAGE, new BlockProtectionListener(waypoints),
				Priority.High);
		addListener(Type.BLOCK_PLACE, new BlockProtectionListener(waypoints),
				Priority.High);
		addListener(Type.ENTITY_EXPLODE, new ProtectionEntityListener(this,
				waypoints), Priority.High);

		addCommandHandler(new WaypointCreateHandler(this));
		addCommandHandler(new WaypointListHandler(this));
		addCommandHandler(new WaypointUseHandler(this));
		addCommandHandler(new WaypointDestroyHandler(this));

	}

	private void setupDatabase() {
		try {
			getDatabase().find(Waypoint.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Waypoint database");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(Waypoint.class);

		return classes;
	}

	@Override
	public void onDisable() {
		logger.info("[Waypoint] Disabled Waypoint plugin");
	}

	public Waypoints getWaypoints() {
		return waypoints;
	}
}
