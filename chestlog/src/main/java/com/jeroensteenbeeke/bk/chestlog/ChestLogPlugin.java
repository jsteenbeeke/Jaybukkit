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
package com.jeroensteenbeeke.bk.chestlog;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.chestlog.commands.GetChestLogHandler;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestData;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLocation;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLog;
import com.jeroensteenbeeke.bk.chestlog.listeners.ChestBlockListener;
import com.jeroensteenbeeke.bk.chestlog.listeners.PlayerInteractListener;

public class ChestLogPlugin extends JSPlugin {
	public static final String PERMISSION_CHESTLOG_VIEW = "chestlog.view";
	public static final String PERMISSION_BREAK_CHEST = "chestlog.remove";
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled chestlog plugin");

		setupDatabase();

		addCommandHandler(new GetChestLogHandler(this));
		ChestBlockListener listener = new ChestBlockListener(getDatabase());

		addListener(Type.BLOCK_PLACE, listener, Priority.Normal);
		addListener(Type.BLOCK_DAMAGE, listener, Priority.Highest);
		addListener(Type.BLOCK_BREAK, listener, Priority.Highest);
		addListener(Type.PLAYER_INTERACT, new PlayerInteractListener(
				getDatabase()), Priority.Normal);
	}

	private void setupDatabase() {
		try {
			getDatabase().find(ChestData.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Chestlog database");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(ChestData.class);
		classes.add(ChestLocation.class);
		classes.add(ChestLog.class);

		return classes;
	}

	@Override
	public void onDisable() {

	}
}
