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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public final class Waypoints {
	private final WaypointPlugin plugin;

	private Set<WaypointLocation> locations;

	private Map<WaypointLocation, Waypoint> locationToWaypointMap;

	private Map<String, Waypoint> nameToWaypointMap;

	public Waypoints(WaypointPlugin plugin) {
		this.plugin = plugin;
		this.locations = new HashSet<WaypointLocation>();
		this.locationToWaypointMap = new HashMap<Waypoints.WaypointLocation, Waypoint>();
		this.nameToWaypointMap = new HashMap<String, Waypoint>();

		init();
	}

	private void init() {
		EbeanServer database = plugin.getDatabase();

		Query<Waypoint> q = database.createQuery(Waypoint.class);

		List<Waypoint> waypoints = q.findList();

		for (Waypoint waypoint : waypoints) {
			addWaypoint(waypoint);

		}
	}

	/**
	 * Creates a new waypoint, assuming the location has already been checked for
	 * suitability, and any world mutations required are already done elsewhere
	 * 
	 * @param location
	 *            the location of the waypoint
	 * @param name
	 *            the name of the waypoint
	 */
	public void newWaypoint(Location location, String name) {
		Waypoint waypoint = new Waypoint();
		waypoint.setName(name);
		waypoint.setWorldName(location.getWorld().getName());
		waypoint.setX(location.getBlockX());
		waypoint.setY(location.getBlockY());
		waypoint.setZ(location.getBlockZ());
		plugin.getDatabase().save(waypoint);

		addWaypoint(waypoint);

	}

	private void addWaypoint(Waypoint waypoint) {
		nameToWaypointMap.put(waypoint.getName(), waypoint);
		for (int x = waypoint.getX() - 2; x <= waypoint.getX() + 2; x++) {
			for (int z = waypoint.getZ() - 2; z <= waypoint.getZ() + 2; z++) {
				if (Math.abs(x) != 2 || Math.abs(z) != 2) {
					WaypointLocation waypointLocation = new WaypointLocation(x,
							waypoint.getY(), z);
					locations.add(waypointLocation);
					locationToWaypointMap.put(waypointLocation, waypoint);
				}
			}
		}
	}

	public Waypoint getWaypoint(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		if (isWaypoint(x, y, z)) {
			Waypoint wp = locationToWaypointMap.get(new WaypointLocation(x, y,
					z));
			if (wp == null) {
				wp = locationToWaypointMap
						.get(new WaypointLocation(x, y - 1, z));
			}

			if (wp != null) {
				if (wp.getWorldName().equals(location.getWorld().getName())) {
					return wp;
				}
			}
		}

		return null;
	}

	public boolean isWaypoint(int x, int y, int z) {
		return locations.contains(new WaypointLocation(x, y, z))
				|| locations.contains(new WaypointLocation(x, y - 1, z))
				|| locations.contains(new WaypointLocation(x, y + 1, z));
	}

	private static class WaypointLocation {
		private final int x;

		private final int y;

		private final int z;

		public WaypointLocation(int x, int y, int z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			result = prime * result + z;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WaypointLocation other = (WaypointLocation) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			if (z != other.z)
				return false;
			return true;
		}

	}

	public Waypoint getWaypoint(String target) {
		if (nameToWaypointMap.containsKey(target))
			return nameToWaypointMap.get(target);

		return null;
	}

	public List<Waypoint> getWaypointsByWorld(String name) {
		List<Waypoint> result = new ArrayList<Waypoint>(
				nameToWaypointMap.size());

		for (Waypoint waypoint : nameToWaypointMap.values()) {
			if (waypoint.getWorldName().equals(name)) {
				result.add(waypoint);
			}
		}
		return result;
	}

	public void destroy(Waypoint wp) {
		List<WaypointLocation> toRemove = new ArrayList<WaypointLocation>(21);
		for (Entry<WaypointLocation, Waypoint> e : locationToWaypointMap
				.entrySet()) {
			if (e.getValue().equals(wp)) {
				toRemove.add(e.getKey());

			}
		}

		for (WaypointLocation loc : toRemove) {
			locationToWaypointMap.remove(loc);
			locations.remove(loc);
		}

		nameToWaypointMap.remove(wp.getName());

	}
}
