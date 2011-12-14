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

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class FetchActivator extends ServerListener {
	private final PermissionsFetcher fetcher;

	public FetchActivator(PermissionsFetcher fetcher) {
		this.fetcher = fetcher;
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getClass() == PermissionsPlugin.class) {
			fetcher.permissionsEnabled((PermissionsPlugin) event.getPlugin());
		}
	}
}
