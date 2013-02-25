package com.jeroensteenbeeke.bukkit.killzone;

import java.util.Map;
import java.util.Set;

import org.bukkit.Location;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

public class ZoneTracker {
	private final Map<String, Table<Integer, Integer, Set<Integer>>> killzones;

	public ZoneTracker() {
		this.killzones = Maps.newHashMap();
	}

	public boolean isKillZone(Location location) {
		final String worldname = location.getWorld().getName();
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();

		if (killzones.containsKey(worldname)) {
			if (killzones.get(worldname).contains(x, z)) {
				return killzones.get(worldname).get(x, z).contains(y);
			}

		}
		return false;
	}

	public void registerBlock(String world, int x, int y, int z) {
		if (!killzones.containsKey(world)) {
			killzones.put(world,
					HashBasedTable.<Integer, Integer, Set<Integer>> create());
		}

		if (!killzones.get(world).contains(x, z)) {
			killzones.get(world).put(x, z, Sets.<Integer> newHashSet());
		}

		killzones.get(world).get(x, z).add(y);

	}
}
