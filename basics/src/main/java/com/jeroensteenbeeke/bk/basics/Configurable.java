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
package com.jeroensteenbeeke.bk.basics;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

@SuppressWarnings("deprecation")
public abstract class Configurable implements IConfigurable {
	private Logger logger = Logger.getLogger("Minecraft");

	private final JSPlugin plugin;

	public Configurable(JSPlugin plugin) {
		this.plugin = plugin;
	}

	protected final JSPlugin getPlugin() {
		return plugin;
	}

	public final ConfigurationNode getConfigFile(String name) {
		synchronized (plugin) {
			File folder = plugin.getDataFolder();
			if (!folder.exists()) {
				folder.mkdir();
			}

			File config = new File(folder, name);
			try {
				if (!config.exists())
					config.createNewFile();

				Configuration cfg = new Configuration(config);
				cfg.load();

				return cfg;
			} catch (IOException ioe) {
				logger.log(
						Level.SEVERE,
						"Could not create balance storage file: "
								+ ioe.getMessage());

				return Configuration.getEmptyNode();
			}
		}
	}

	public final void saveConfigFile(String name, ConfigurationNode node) {
		synchronized (plugin) {
			if (node instanceof Configuration) {
				Configuration config = (Configuration) node;

				config.save();
			} else {
				logger.log(Level.WARNING, "Unsaveable configuration");
			}
		}
	}
}
