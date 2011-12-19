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
package com.jeroensteenbeeke.bk.spleefregen.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefCreateHandler extends PlayerAwareCommandHandler {

	private final SpleefRegen plugin;

	public SpleefCreateHandler(SpleefRegen plugin) {
		super(plugin.getServer(), SpleefRegen.PERMISSION_CREATE_SPLEEF);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("spleefgen").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {

		return ifArgCountIs(2).andArgumentEquals(1, "dirt", "snow", "wool",
				"nether").itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

		if (player != null) {
			List<Location> locations = plugin.compileLocations(
					player.getWorld(), player.getLocation());

			if (locations.isEmpty()) {
				Messages.send(player,
						"&cUnsuitable location, or area too large");
				return;
			} else {
				String name = args[0];
				int total = plugin.getDatabase().createQuery(SpleefPoint.class)
						.where().eq("name", name).findRowCount();

				if (total == 0) {
					int type = 0;
					if ("dirt".equals(args[1])) {
						type = 3;
					} else if ("snow".equals(args[1])) {
						type = 80;
					} else if ("wool".equals(args[1])) {
						type = 35;
					} else if ("nether".equals(args[1])) {
						type = 83;
					} else {

						Messages.send(
								player,
								"&cInvalid material: &e"
										+ args[1]
										+ "&c, only the following are allowed: &edirt&c, &esnow&c, &ewool&c, &enether");
						return;
					}

					player.teleport(player.getLocation().add(0.0, 2.0, 0.0));

					SpleefPoint point = new SpleefPoint();
					point.setName(name);
					point.setMaterial(type);
					point.setWorld(player.getWorld().getName());
					point.setLocked(true);
					plugin.getDatabase().save(point);

					for (Location location : locations) {
						SpleefLocation loc = new SpleefLocation();
						loc.setPoint(point);
						loc.setX(location.getBlockX());
						loc.setY(location.getBlockY());
						loc.setZ(location.getBlockZ());
						plugin.getDatabase().save(loc);
						if (location.getBlock().getTypeId() != type) {
							location.getBlock().setTypeId(type);
						}
					}
				} else {
					Messages.send(player, "&cName taken");
				}
			}
		} else {
			Messages.send(player,
					"&cYou do not have the capability to create spleef areas");
		}
	}

}
