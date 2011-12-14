package com.jeroensteenbeeke.bk.ville;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public abstract class AbstractVilleCommandHandler extends
		PlayerAwareCommandHandler {
	private Ville ville;

	public AbstractVilleCommandHandler(Ville ville, PermissionPolicy policy,
			String... requiredPermissions) {
		super(ville.getServer(), policy, requiredPermissions);
		this.ville = ville;
	}

	public AbstractVilleCommandHandler(Ville ville,
			String... requiredPermissions) {
		super(ville.getServer(), requiredPermissions);
		this.ville = ville;
	}

	protected final Ville getVille() {
		return ville;
	}

	protected final List<VillageLocation> getClosestLocations(Location location) {
		List<VillageLocation> locationsInRange = ville
				.getDatabase()
				.find(VillageLocation.class)
				.where()
				.between("x", location.getBlockX() - 1000,
						location.getBlockX() + 1000)
				.between("z", location.getBlockZ() - 1000,
						location.getBlockZ() + 1000)
				.eq("world", location.getWorld().getName()).findList();

		List<VillageLocation> result = new ArrayList<VillageLocation>(
				locationsInRange.size());

		for (VillageLocation loc : locationsInRange) {
			if (distance(loc, location) < ville.getMinimumDistance()) {
				result.add(loc);
			}
		}

		return result;
	}

	protected final int distance(VillageLocation loc, Location location) {
		int xDist = Math.max(loc.getX(), location.getBlockX())
				- Math.min(loc.getX(), location.getBlockX());
		int yDist = Math.max(loc.getY(), location.getBlockY())
				- Math.min(loc.getY(), location.getBlockY());
		int zDist = Math.max(loc.getZ(), location.getBlockZ())
				- Math.min(loc.getZ(), location.getBlockZ());

		BigDecimal xd = new BigDecimal(xDist);
		BigDecimal yd = new BigDecimal(yDist);
		BigDecimal zd = new BigDecimal(zDist);

		BigDecimal x2_z2 = xd.pow(2).add(zd.pow(2));
		BigDecimal xzDist = new BigDecimal(Math.sqrt(x2_z2.doubleValue()));
		BigDecimal xz2_y2 = xzDist.pow(2).add(yd.pow(2));

		return new BigDecimal(Math.sqrt(xz2_y2.doubleValue())).intValue();
	}
}
