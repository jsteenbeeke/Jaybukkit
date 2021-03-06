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
package com.jeroensteenbeeke.bk.blockhistory;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.avaje.ebean.Query;
import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.blockhistory.commands.GetHistoryCommand;
import com.jeroensteenbeeke.bk.blockhistory.commands.RevertCommand;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange.BlockChangeType;
import com.jeroensteenbeeke.bk.blockhistory.listeners.BlockHistoryEntityListener;
import com.jeroensteenbeeke.bk.blockhistory.listeners.BlockHistoryListener;

public class BlockHistory extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final String PERMISSION_VIEW_HISTORY = "blockhistory.view";

	public static final String PERMISSION_REVERT_HISTORY = "blockhistory.revert";

	@Override
	public void onEnable() {
		logger.info("Enabled blockhistory plugin");

		setupDatabase();

		addCommandHandler(new GetHistoryCommand(this));
		addCommandHandler(new RevertCommand(this));

		addListener(new BlockHistoryListener(this));
		addListener(new BlockHistoryEntityListener(this));
	}

	@Override
	public void onDisable() {

	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(BlockChange.class);

		return classes;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(BlockChange.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing block history database");
			installDDL();
		}
	}

	public Query<BlockChange> getBaseQuery(Block block) {
		return getBaseQuery(block.getX(), block.getY(), block.getZ(), block
				.getWorld().getName());
	}

	public Query<BlockChange> getBaseQuery(int x, int y, int z, String world) {
		return getDatabase().createQuery(BlockChange.class).where().eq("x", x)
				.eq("y", y).eq("z", z).eq("world", world).order()
				.desc("changeDate").setMaxRows(5);
	}

	private boolean hasHistory(int x, int y, int z, String world) {
		return getBaseQuery(x, y, z, world).findRowCount() > 0;
	}

	public void addBlock(Block b, String plyer, BlockChangeType changeType) {
		addBlock(b.getX(), b.getY(), b.getZ(), b.getTypeId(), b.getWorld()
				.getName(), plyer, changeType);
	}

	public void removeBlock(Block b, String plyer, BlockChangeType changeType) {
		removeBlock(b.getX(), b.getY(), b.getZ(), b.getTypeId(), b.getWorld()
				.getName(), plyer, changeType);
	}

	public void removeBlock(int x, int y, int z, int typeId, String world,
			String player, BlockChangeType changeType) {
		BlockChange old = hasHistory(x, y, z, world) ? getBaseQuery(x, y, z,
				world).findList().get(0) : saveBlockData(x, y, z, typeId,
				world, null, BlockChangeType.GENERATED);

		BlockChange n = saveBlockData(x, y, z, Material.AIR.getId(), world,
				player, changeType);
		n.setOverrides(old);
		getDatabase().update(n);
	}

	public void addBlock(int x, int y, int z, int typeId, String world,
			String player, BlockChangeType changeType) {
		BlockChange old = hasHistory(x, y, z, world) ? getBaseQuery(x, y, z,
				world).findList().get(0) : saveBlockData(x, y, z,
				Material.AIR.getId(), world, null, BlockChangeType.GENERATED);

		BlockChange n = saveBlockData(x, y, z, typeId, world, player,
				changeType);
		n.setOverrides(old);
		getDatabase().update(n);
	}

	private BlockChange saveBlockData(int x, int y, int z, int typeId,
			String world, String player, BlockChangeType changeType) {
		BlockChange change = new BlockChange();
		change.setChangeType(changeType);
		change.setCulprit(player);
		if (changeType == BlockChangeType.GENERATED) {
			change.setChangeDate(new Date(System.currentTimeMillis() - 3600000L));
		} else {
			change.setChangeDate(new Date());
		}
		change.setWorld(world);
		change.setX(x);
		change.setY(y);
		change.setZ(z);
		change.setBlockType(typeId);
		getDatabase().save(change);

		return change;
	}
}
