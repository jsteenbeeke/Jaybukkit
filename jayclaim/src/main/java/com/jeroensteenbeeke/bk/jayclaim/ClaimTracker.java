package com.jeroensteenbeeke.bk.jayclaim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.avaje.ebean.EbeanServer;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.bk.basics.spatial.SpatialKey;
import com.jeroensteenbeeke.bk.basics.spatial.SpatialKeys;
import com.jeroensteenbeeke.bk.basics.spatial.SpatialMap;
import com.jeroensteenbeeke.bk.jayclaim.entities.Claim;

public class ClaimTracker {
	private Logger logger = Logger.getLogger("Minecraft");

	private final Map<String, Map<SpatialKey, Claim>> claims;

	private final int claimSize;

	private final EbeanServer database;

	private final File backupFolder;

	ClaimTracker(EbeanServer database, int claimSize, final File backupFolder) {
		this.claims = Maps.newHashMap();
		this.claimSize = claimSize;
		this.database = database;
		this.backupFolder = backupFolder;

		mapClaims();

	}

	synchronized void mapClaims() {
		claims.clear();

		for (Claim claim : database.find(Claim.class).findList()) {
			final int x = claim.getX();
			final int z = claim.getZ();
			final String world = claim.getWorld();

			if (!claims.containsKey(world)) {
				claims.put(world, new SpatialMap<SpatialKey, Claim>(2, 10));
			}

			for (SpatialKey key : getCorners(x, z)) {
				claims.get(world).put(key, claim);
			}
		}
	}

	public Claim getClaimAt(Location location) {
		final String world = location.getWorld().getName();

		if (claims.containsKey(world)) {
			for (SpatialKey corner : getCorners(location)) {
				Claim claim = claims.get(world).get(corner);
				if (claim != null)
					return claim;
			}
		}

		return null;
	}

	private SpatialKey[] getCorners(Location location) {
		return getCorners(location.getBlockX(), location.getBlockZ());
	}

	private SpatialKey[] getCorners(int x, int z) {
		int baseX = getBase(x);
		int topX = x >= 0 ? baseX + claimSize : baseX - claimSize;

		int baseZ = getBase(z);
		int topZ = z >= 0 ? baseZ + claimSize : baseZ - claimSize;

		SpatialKey[] keys = new SpatialKey[4];
		keys[0] = SpatialKeys.keyFor(baseX, baseZ);
		keys[1] = SpatialKeys.keyFor(baseX, topZ);
		keys[2] = SpatialKeys.keyFor(topX, baseZ);
		keys[3] = SpatialKeys.keyFor(topX, topZ);

		return keys;
	}

	protected final int getBase(int real) {
		return claimSize * (real / claimSize);
	}

	public void storeClaimBackup(Claim claim, World world) {
		int baseX = getBase(claim.getX());
		long topX = claim.getX() >= 0 ? baseX + claimSize : baseX - claimSize;

		int baseZ = getBase(claim.getZ());
		long topZ = claim.getZ() >= 0 ? baseZ + claimSize : baseZ - claimSize;

		int maxY = world.getMaxHeight();

		Properties props = new Properties();

		for (int y = 0; y <= maxY; y++) {
			for (int z = baseZ; z <= topZ; z++) {
				for (int x = baseX; x <= topX; x++) {
					Block blockAt = world.getBlockAt(x, y, z);
					props.put(
							String.format("%d,%d,%d", x, y, z),
							String.format("%d:%d", blockAt.getTypeId(),
									blockAt.getData()));
				}
			}
		}

		File file = new File(backupFolder, String.format("claim-%d.xml"));

		try {
			props.storeToXML(new FileOutputStream(file),
					String.format("Claim backup for claim %d", claim.getId()));
		} catch (FileNotFoundException e) {
			logger.severe("Could not write backup to file" + file.getName());
		} catch (IOException e) {
			logger.severe("Could not write backup to file" + file.getName()
					+ ": " + e.getMessage());
		}

	}
}
