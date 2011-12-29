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
package com.jeroensteenbeeke.bk.permfetch;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class PermissionsFetcher extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");
	private PermissionsPlugin permissionsPlugin;

	final static String DEFAULT_URL = "ENTER URL HERE";

	private String url;

	@Override
	public void onLoad() {
		getConfig().addDefault("url", DEFAULT_URL);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled permissionfetcher plugin");

		this.url = getConfig().getString("url", DEFAULT_URL);

		if (!DEFAULT_URL.equals(url)) {
			addListener(Type.PLUGIN_ENABLE, new FetchActivator(this),
					Priority.Highest);

		} else {
			logger.severe("Invalid URL for permissions fetching, will ignore!");
		}

	}

	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
	}

	public PermissionsPlugin getPermissionsPlugin() {
		return permissionsPlugin;
	}

	public void permissionsEnabled(PermissionsPlugin plugin) {
		this.permissionsPlugin = plugin;

		FetcherTask ft = new FetcherTask(this, url);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, ft,
				60L * 20L, 3600L * 20L);
	}
}
