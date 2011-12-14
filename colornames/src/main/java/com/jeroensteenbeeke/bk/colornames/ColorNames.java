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
package com.jeroensteenbeeke.bk.colornames;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class ColorNames extends JSPlugin
{
	private Logger logger = Logger.getLogger("Minecraft");

	static final String PERMISSION_PREFIX = "colornames.";

	@Override
	public void onEnable()
	{
		logger.info("Enabled colornames plugin");

		addListener(Type.PLAYER_CHAT, new ChatColorListener(), Priority.Normal);
	}

	@Override
	public void onDisable()
	{

	}
}
