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
package com.jeroensteenbeeke.bk.premium;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.premium.listeners.PremiumPlayerListener;

public class PremiumMembers extends JSPlugin {
	public static final String PERMISSION_PREMIUM = "premiummembers.premium";

	public static final String PERMISSION_REGULAR = "premiummembers.regular";

	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onLoad() {
		getConfig().addDefault("premiumslots", 8);
		getConfig().addDefault("guestslots", 6);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled premium members plugin");

		int premium = getConfig().getInt("premiumslots", 8);
		int guest = getConfig().getInt("guestslots", 6);

		saveConfig();

		addListener(Type.PLAYER_JOIN, new PremiumPlayerListener(this, premium,
				guest), Priority.Lowest);
	}

	@Override
	public void onDisable() {

	}
}
