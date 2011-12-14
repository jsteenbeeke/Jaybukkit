package com.jeroensteenbeeke.bk.spleefregen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefCreateHandler;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefDeleteHandler;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefFloodHandler;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefListHandler;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefLockHandler;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefRegenerateHandler;
import com.jeroensteenbeeke.bk.spleefregen.commands.SpleefUnlockHandler;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;
import com.jeroensteenbeeke.bk.spleefregen.listeners.SpleefBlockListener;

public class SpleefRegen extends JSPlugin {
	public static final String PERMISSION_CREATE_SPLEEF = "spleefregen.create";

	public static final String PERMISSION_REGENERATE_SPLEEF = "spleefregen.regenerate";

	public static final String PERMISSION_SPLEEF_REMOVE = "spleefregen.remove";

	public static final String PERMISSION_SPLEEF_LOCK = "spleefregen.lock";

	public static final String PERMISSION_SPLEEF_LIST = "spleefregen.list";

	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled spleefregen plugin");

		setupDatabase();

		addCommandHandler(new SpleefCreateHandler(this));
		addCommandHandler(new SpleefRegenerateHandler(this));
		addCommandHandler(new SpleefDeleteHandler(this));
		addCommandHandler(new SpleefLockHandler(this));
		addCommandHandler(new SpleefUnlockHandler(this));
		addCommandHandler(new SpleefListHandler(this));
		addCommandHandler(new SpleefFloodHandler(this));
		addListener(Type.BLOCK_PLACE, new SpleefBlockListener(this),
				Priority.Low);
		addListener(Type.BLOCK_BREAK, new SpleefBlockListener(this),
				Priority.Low);

	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(SpleefPoint.class);
		classes.add(SpleefLocation.class);

		return classes;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(SpleefPoint.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing spleef database");
			installDDL();
		}
		checkIndex(false, "spleef_point", "idx_world", "world");
		checkIndex(false, "spleef_location", "idx_location", "x", "y", "z");
	}

	@Override
	public void onDisable() {

	}

	public List<Location> compileLocations(World world, Location location) {
		List<Location> suitable = new ArrayList<Location>(250);

		Set<Location> explored = new HashSet<Location>();
		explored.add(location);

		Set<Location> toExplore = new HashSet<Location>();
		toExplore.addAll(adjacent(world, location));

		while (!toExplore.isEmpty() && explored.size() < 2500) {
			toExplore.removeAll(explored);

			if (!toExplore.isEmpty()) {
				Location next = toExplore.iterator().next();
				if (next.getBlock().isEmpty()) {
					suitable.add(next);

					toExplore.addAll(adjacent(world, next));
				}

				explored.add(next);
			}
		}

		if (explored.size() == 2500) {
			return new ArrayList<Location>(0);
		}

		return suitable;
	}

	private Set<Location> adjacent(World world, Location location) {
		Set<Location> adjacent = new HashSet<Location>();
		adjacent.add(new Location(world, location.getX() + 1, location.getY(),
				location.getZ()));
		adjacent.add(new Location(world, location.getX() - 1, location.getY(),
				location.getZ()));
		adjacent.add(new Location(world, location.getX(), location.getY(),
				location.getZ() + 1));
		adjacent.add(new Location(world, location.getX(), location.getY(),
				location.getZ() - 1));

		return adjacent;
	}
}
