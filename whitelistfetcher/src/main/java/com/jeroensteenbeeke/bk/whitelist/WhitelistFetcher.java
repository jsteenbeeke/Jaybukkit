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
package com.jeroensteenbeeke.bk.whitelist;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class WhitelistFetcher extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	final static String DEFAULT_URL = "ENTER URL HERE";

	@Override
	public void onEnable() {
		logger.info("Enabled whitelistfetcher plugin");

		String url = getConfig().getString("url", DEFAULT_URL);
		saveConfig();

		if (!DEFAULT_URL.equals(url)) {
			FetcherTask ft = new FetcherTask(this, url);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, ft,
					60L * 20L, 3600L * 20L);

		} else {
			logger.severe("Invalid URL for whitelist fetching, will ignore!");
		}

	}

	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
	}
}
