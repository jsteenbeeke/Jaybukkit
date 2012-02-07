/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.waypoint;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

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

		addListener(new MovementListener(waypoints));
		addListener(new BlockProtectionListener(waypoints));
		addListener(new ProtectionEntityListener(this, waypoints));

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
