package com.jeroensteenbeeke.bk.jaylock;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.avaje.ebean.EbeanServer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jaylock.commands.DisownPlayerCommand;
import com.jeroensteenbeeke.bk.jaylock.commands.MarkLocationForbiddenCommand;
import com.jeroensteenbeeke.bk.jaylock.commands.SetLocationOwnerCommand;
import com.jeroensteenbeeke.bk.jaylock.commands.UnsuitableLocationException;
import com.jeroensteenbeeke.bk.jaylock.entities.ForbiddenLocation;
import com.jeroensteenbeeke.bk.jaylock.events.ForbiddenLocationHandler;

public class Jaylock extends JSPlugin {
	public static final String PERMISSION_JAYLOCK_ADMIN = "jaylock.admin";

	private static final BlockFace[] NESWUD = new BlockFace[] {
			BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
			BlockFace.UP, BlockFace.DOWN };

	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled jaylock plugin");
		setupDatabase();

		addListener(new ForbiddenLocationHandler(this));
		addCommandHandler(new MarkLocationForbiddenCommand(this));
		addCommandHandler(new DisownPlayerCommand(this));
		addCommandHandler(new SetLocationOwnerCommand(this));
	}

	@Override
	public void onDisable() {

	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(ForbiddenLocation.class);

		return classes;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(ForbiddenLocation.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Jaylock database");
			installDDL();
			checkIndex(true, "ForbiddenLocation", "IDX_LOCATION", "x", "y",
					"z", "world");
		}
	}

	public ForbiddenLocation getForbiddenLocation(Block block) {
		EbeanServer database = getDatabase();

		return database.createQuery(ForbiddenLocation.class).where()
				.eq("x", block.getX()).eq("y", block.getY())
				.eq("z", block.getZ()).ieq("world", block.getWorld().getName())
				.findUnique();
	}

	public List<Block> getTargetBlocks(Block b)
			throws UnsuitableLocationException {
		Set<Block> result = Sets.newHashSet();
		Set<Block> toExplore = Sets.newHashSet(b);

		while (!toExplore.isEmpty()) {
			if (result.size() > 250)
				throw new UnsuitableLocationException("area too large");

			Block n = toExplore.iterator().next();
			toExplore.remove(n);

			switch (n.getType()) {
			case WOODEN_DOOR:
			case WOOD_DOOR:
			case IRON_DOOR_BLOCK:
			case IRON_DOOR:
			case BEDROCK:
			case OBSIDIAN:
				continue;
			default:
				for (BlockFace bf : NESWUD) {
					Block rel = n.getRelative(bf);
					if (!result.contains(rel))
						toExplore.add(rel);
				}
			}

			result.add(n);

		}

		if (result.size() < 4)
			throw new UnsuitableLocationException("area too small");

		return Lists.newArrayList(result);
	}
}
