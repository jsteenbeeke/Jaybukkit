package com.jeroensteenbeeke.bk.ville;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;
import com.jeroensteenbeeke.bk.ville.entities.VilleBuilder;

public class VilleLocations {
	private final Map<String, Map<XYZCoordinate, VillageLocation>> jurisdictions;

	private final Map<String, Multimap<XYZCoordinate, VillageLocation>> proximities;

	private final Multimap<VillageLocation, String> builders;

	private final Ville ville;

	public VilleLocations(Ville ville) {
		this.ville = ville;
		this.jurisdictions = Maps.newTreeMap();
		this.proximities = Maps.newTreeMap();
		builders = LinkedListMultimap.create();
		initLocations(ville.getDatabase().find(VillageLocation.class)
				.findList());
	}

	private void initLocations(List<VillageLocation> foundLocations) {
		int minDist = ville.getMinimumDistance();

		Multimap<String, VillageLocation> locsPerWorld = LinkedListMultimap
				.create();

		for (VillageLocation loc : foundLocations) {
			locsPerWorld.put(loc.getWorld(), loc);
		}

		for (Entry<String, Collection<VillageLocation>> e : locsPerWorld
				.asMap().entrySet()) {
			Multimap<VillageLocation, XYZCoordinate> claimed = LinkedListMultimap
					.create();
			String world = e.getKey();

			for (VillageLocation loc : e.getValue()) {
				claimed.putAll(getClaimedCoordinates(minDist, loc));
				for (VilleBuilder builder : loc.getBuilders()) {
					builders.put(loc, builder.getPlayer());
				}
			}

			Map<XYZCoordinate, VillageLocation> jcoords = Maps.newHashMap();
			Multimap<XYZCoordinate, VillageLocation> pcoords = LinkedListMultimap
					.create();

			for (Entry<VillageLocation, XYZCoordinate> ce : claimed.entries()) {
				if (jcoords.containsKey(ce.getValue())) {
					// Contested
					VillageLocation existing = jcoords.get(ce.getValue());
					VillageLocation current = ce.getKey();

					XYZCoordinate existingLoc = XYZCoordinate.get(existing);
					XYZCoordinate currentLoc = XYZCoordinate.get(current);

					int eDist = existingLoc.distanceTo(ce.getValue());
					int nDist = currentLoc.distanceTo(ce.getValue());

					if (nDist < eDist) {
						jcoords.put(ce.getValue(), current);
					}

				} else {
					jcoords.put(ce.getValue(), ce.getKey());
				}
			}

			jurisdictions.put(world, jcoords);
			proximities.put(world, pcoords);
		}
	}

	public boolean hasBuilderPermission(Player player, Location location) {
		VillageLocation loc = getJurisdiction(location);
		if (loc != null) {
			if (loc.isRestricted()) {

				return loc.getOwner().equals(player.getName())
						|| builders.containsEntry(loc, player.getName());
			}
		}

		return true;
	}

	public void remapJurisdictions() {
		jurisdictions.clear();
		proximities.clear();
		builders.clear();

		initLocations(ville.getDatabase().find(VillageLocation.class)
				.findList());
	}

	public VillageLocation getJurisdiction(Location location) {
		XYZCoordinate coord = XYZCoordinate.get(location);
		String world = location.getWorld().getName();

		if (jurisdictions.containsKey(world)) {
			return jurisdictions.get(world).get(coord);
		}

		return null;

	}

	public List<VillageLocation> getNearbyVillages(Location location) {
		XYZCoordinate coord = XYZCoordinate.get(location);
		String world = location.getWorld().getName();

		if (jurisdictions.containsKey(world)) {
			Collection<VillageLocation> locs = proximities.get(world)
					.get(coord);

			if (locs != null) {
				return new ArrayList<VillageLocation>(locs);
			}
		}

		return Collections.emptyList();

	}

	private Multimap<VillageLocation, XYZCoordinate> getClaimedCoordinates(
			int minDist, VillageLocation loc) {
		XYZCoordinate center = XYZCoordinate.get(loc);

		Multimap<VillageLocation, XYZCoordinate> claimed = LinkedListMultimap
				.create();

		int minX = center.getX() - minDist;
		int maxX = center.getX() + minDist;
		int minZ = center.getZ() - minDist;
		int maxZ = center.getZ() + minDist;
		int minY = center.getY() - minDist;
		int maxY = center.getY() + minDist;

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					XYZCoordinate c = new XYZCoordinate(x, y, z);
					if (center.distanceTo(c) <= minDist) {
						claimed.put(loc, c);
					}
				}
			}
		}

		return claimed;
	}

	private static final class XYZCoordinate {
		private final int x;

		private final int y;

		private final int z;

		public XYZCoordinate(int x, int y, int z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public static XYZCoordinate get(Location location) {
			return new XYZCoordinate(location.getBlockX(),
					location.getBlockY(), location.getBlockZ());
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
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
			XYZCoordinate other = (XYZCoordinate) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			if (z != other.z)
				return false;
			return true;
		}

		public int distanceTo(XYZCoordinate other) {
			int xDist = Math.max(getX(), other.getX())
					- Math.min(getX(), other.getX());
			int yDist = Math.max(getY(), other.getY())
					- Math.min(getY(), other.getY());
			int zDist = Math.max(getZ(), other.getZ())
					- Math.min(getZ(), other.getZ());

			BigDecimal xd = new BigDecimal(xDist);
			BigDecimal yd = new BigDecimal(yDist);
			BigDecimal zd = new BigDecimal(zDist);

			BigDecimal x2_z2 = xd.pow(2).add(zd.pow(2));
			BigDecimal xzDist = new BigDecimal(Math.sqrt(x2_z2.doubleValue()));
			BigDecimal xz2_y2 = xzDist.pow(2).add(yd.pow(2));

			return new BigDecimal(Math.sqrt(xz2_y2.doubleValue())).intValue();
		}

		public static XYZCoordinate get(VillageLocation vl) {
			return new XYZCoordinate(vl.getX(), vl.getY(), vl.getZ());
		}

	}

	public int getDistance(VillageLocation vl, Location l) {
		if (!vl.getWorld().equals(l.getWorld().getName())) {
			return -1;
		}

		XYZCoordinate lc = XYZCoordinate.get(l);
		XYZCoordinate vlc = XYZCoordinate.get(vl);

		return lc.distanceTo(vlc);
	}

	public void removeBuilder(VillageLocation location, String targetPlayer) {
		builders.remove(location, targetPlayer);
	}

	public void addBuilder(VillageLocation location, String targetPlayer) {
		builders.put(location, targetPlayer);
	}

}
