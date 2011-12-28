package com.jeroensteenbeeke.bk.ville;

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
	private final Map<String, Map<SpatialIndex, VillageLocation>> jurisdictions;

	private final Multimap<VillageLocation, String> builders;

	private final Ville ville;

	public VilleLocations(Ville ville) {
		this.ville = ville;
		this.jurisdictions = Maps.newTreeMap();
		builders = LinkedListMultimap.create();
		initLocations(ville.getDatabase().find(VillageLocation.class)
				.findList());
	}

	private void initLocations(List<VillageLocation> foundLocations) {
		Multimap<String, VillageLocation> locsPerWorld = LinkedListMultimap
				.create();

		for (VillageLocation loc : foundLocations) {
			locsPerWorld.put(loc.getWorld(), loc);
		}

		for (Entry<String, Collection<VillageLocation>> e : locsPerWorld
				.asMap().entrySet()) {
			Map<SpatialIndex, VillageLocation> coords = Maps.newHashMap();
			String world = e.getKey();

			for (VillageLocation loc : e.getValue()) {
				XYZCoordinate xyz = XYZCoordinate.get(loc);
				SpatialIndex index = new SpatialIndex(
						ville.getMinimumDistance(), xyz.getX(), xyz.getY(),
						xyz.getZ());

				coords.put(index, loc);
				for (VilleBuilder builder : loc.getBuilders()) {
					builders.put(loc, builder.getPlayer());
				}
			}

			jurisdictions.put(world, coords);
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

	public boolean isBuilderAt(Player player, Location location) {
		VillageLocation loc = getJurisdiction(location);
		if (loc != null) {
			return loc.getOwner().equals(player.getName())
					|| builders.containsEntry(loc, player.getName());
		}

		return true;
	}

	public void remapJurisdictions() {
		jurisdictions.clear();
		builders.clear();

		initLocations(ville.getDatabase().find(VillageLocation.class)
				.findList());
	}

	public VillageLocation getJurisdiction(Location location) {
		XYZCoordinate coord = XYZCoordinate.get(location);
		String world = location.getWorld().getName();

		if (jurisdictions.containsKey(world)) {

			int minDist = Integer.MAX_VALUE;
			VillageLocation closestLoc = null;

			for (Entry<SpatialIndex, VillageLocation> loc : jurisdictions.get(
					world).entrySet()) {
				SpatialIndex i = loc.getKey();

				XYZCoordinate locXYZ = XYZCoordinate.get(loc.getValue());

				if (i.contains(coord)) {
					int dist = coord.distanceTo(locXYZ);

					if (dist < ville.getMinimumDistance()) {
						if (closestLoc == null) {
							minDist = dist;
							closestLoc = loc.getValue();
						} else if (minDist > dist) {
							minDist = dist;
							closestLoc = loc.getValue();
						}
					}
				}
			}

			if (closestLoc != null) {
				return closestLoc;
			}
		}

		return null;

	}

	public List<VillageLocation> getNearbyVillages(Location location) {
		XYZCoordinate coord = XYZCoordinate.get(location);
		String world = location.getWorld().getName();

		if (jurisdictions.containsKey(world)) {
			Map<SpatialIndex, VillageLocation> locs = jurisdictions.get(world);

			if (locs != null) {
				List<VillageLocation> found = new ArrayList<VillageLocation>(
						locs.size());

				for (Entry<SpatialIndex, VillageLocation> loc : locs.entrySet()) {
					XYZCoordinate xyz = XYZCoordinate.get(loc.getValue());

					if (loc.getKey().contains(coord)) {
						if (xyz.distanceTo(coord) < ville.getMinimumDistance()) {
							found.add(loc.getValue());
						}
					}
				}

				if (found.size() > 0) {
					return found;
				}
			}
		}

		return Collections.emptyList();

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
