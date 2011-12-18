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
package com.jeroensteenbeeke.bk.ville.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleCheckCommandHandler extends AbstractVilleCommandHandler {
	public VilleCheckCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_USE);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "check").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 1) {

			List<VillageLocation> closestLocations = getLocationsHandle()
					.getNearbyVillages(player.getLocation());
			if (closestLocations.size() == 0) {
				Messages.send(player, "&aThis location is suitable");
			} else {
				Messages.send(player, "&cThis location is unsuitable");
				for (VillageLocation loc : closestLocations) {
					Messages.send(
							player,
							String.format("&e- &cToo close to &e%s",
									loc.getName()));
				}
			}

			return true;
		}

		return false;
	}
}
