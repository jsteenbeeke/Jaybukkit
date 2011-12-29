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
package com.jeroensteenbeeke.bk.dbdump;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.dbdump.commands.RestoreDumpHandler;
import com.jeroensteenbeeke.bk.dbdump.tasks.CleanupTask;
import com.jeroensteenbeeke.bk.dbdump.tasks.DumpTask;

public class DatabaseDump extends JSPlugin {
	public static final String PERMISSION_RESTORE = "dbdump.restore";

	private Logger logger = Logger.getLogger("Minecraft");

	private List<JavaPlugin> plugins;

	private List<Class<?>> dbClasses;

	private int maxDumps;

	private File dumpFolder;

	public DatabaseDump() {

	}

	@Override
	public void onLoad() {
		getConfig().addDefault(Config.MAX_STORED_DUMPS,
				Config.MAX_STORED_DUMPS_DEFAULT);
		getConfig().addDefault(Config.DUMP_INTERVAL,
				Config.DUMP_INTERVAL_DEFAULT);
		getConfig().addDefault(Config.DUMP_FOLDER, Config.DUMP_FOLDER_DEFAULT);
		getConfig().addDefault(Config.DUMP_RELATIVE_TO_PLUGIN_DATA,
				Config.DUMP_RELATIVE_TO_PLUGIN_DATA_DEFAULT);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled dbdump plugin");

		maxDumps = getConfig().getInt(Config.MAX_STORED_DUMPS,
				Config.MAX_STORED_DUMPS_DEFAULT);
		long interval = getConfig().getLong(Config.DUMP_INTERVAL,
				Config.DUMP_INTERVAL_DEFAULT);
		boolean relativeToData = getConfig().getBoolean(
				Config.DUMP_RELATIVE_TO_PLUGIN_DATA,
				Config.DUMP_RELATIVE_TO_PLUGIN_DATA_DEFAULT);
		String dumpFolderName = getConfig().getString(Config.DUMP_FOLDER,
				Config.DUMP_FOLDER_DEFAULT);

		if (relativeToData) {
			dumpFolder = new File(getDataFolder(), dumpFolderName);
		} else {
			dumpFolder = new File(dumpFolderName);
		}

		if (!dumpFolder.exists()) {
			logger.info("Dump folder does not exist, creating");
			dumpFolder.mkdirs();
		}

		dbClasses = new LinkedList<Class<?>>();

		Plugin[] allPlugins = getServer().getPluginManager().getPlugins();

		plugins = new ArrayList<JavaPlugin>(allPlugins.length);

		for (Plugin plugin : allPlugins) {
			if (plugin instanceof JavaPlugin
					&& plugin.getDescription().isDatabaseEnabled()) {

				plugins.add((JavaPlugin) plugin);
				dbClasses.addAll(((JavaPlugin) plugin).getDatabaseClasses());
			}
		}

		addCommandHandler(new RestoreDumpHandler(this));

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new DumpTask(this), 20 * 60, 20 * interval);

		if (maxDumps != 0) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new CleanupTask(this), 20 * 60, 20 * interval);
		}
	}

	public List<JavaPlugin> getPlugins() {
		return plugins;
	}

	@Override
	public void onDisable() {

	}

	public int getMaxDumps() {
		return maxDumps;
	}

	public File getDumpFolder() {
		return dumpFolder;
	}

	private static final class Config {
		public static final String MAX_STORED_DUMPS = "maxStored";

		public static final int MAX_STORED_DUMPS_DEFAULT = 20;

		public static final String DUMP_INTERVAL = "dumpInterval";

		public static final long DUMP_INTERVAL_DEFAULT = 60L * 60L * 6L;

		public static final String DUMP_RELATIVE_TO_PLUGIN_DATA = "dumpFolderRelativeToPlugin";

		public static final boolean DUMP_RELATIVE_TO_PLUGIN_DATA_DEFAULT = true;

		public static final String DUMP_FOLDER = "dumpFolder";

		public static final String DUMP_FOLDER_DEFAULT = "dumps";
	}
}
