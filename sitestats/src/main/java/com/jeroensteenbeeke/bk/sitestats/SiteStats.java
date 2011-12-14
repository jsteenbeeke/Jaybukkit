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
package com.jeroensteenbeeke.bk.sitestats;

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class SiteStats extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onLoad() {
		String target = getConfig().getString("targetUrl",
				"https://www.daeshara.com/updateUserList.php");
		getConfig().set("targetUrl", target);
		String key = getConfig().getString("key", "");
		getConfig().set("key", key);

		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled sitestats plugin");

		String key = getConfig().getString("key");

		if (key == null || key.isEmpty()) {
			logger.severe("Please edit the sitestats config.yml file and enter your key");
		} else {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new UpdateSiteTask(this), 20 * 60 * 1, 20 * 60 * 3);
		}
	}

	@Override
	public void onDisable() {

	}
}
