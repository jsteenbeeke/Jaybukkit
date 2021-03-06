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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.util.Messages;

public class FetcherTask implements Runnable {
	private Logger log = Logger.getLogger("Minecraft");

	private PermissionsFetcher plugin;

	private final String url;

	public FetcherTask(PermissionsFetcher permissionsFetcher, String url) {
		this.plugin = permissionsFetcher;
		this.url = url;
	}

	@Override
	public void run() {
		try {
			URLConnection conn = new URL(url).openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			File config = new File(plugin.getPermissionsPlugin()
					.getDataFolder(), "config.yml");

			PrintWriter pw = new PrintWriter(new FileWriter(config));

			String next = null;

			while ((next = reader.readLine()) != null) {
				pw.println(next);
			}
			pw.flush();
			pw.close();

			plugin.getServer()
					.dispatchCommand(plugin.getServer().getConsoleSender(),
							"permissions reload");

			Messages.broadcast(plugin.getServer(), "&aPermissions updated");
		} catch (IOException e) {
			Messages.broadcast(plugin.getServer(),
					"&cFailed to fetch permissions");
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
