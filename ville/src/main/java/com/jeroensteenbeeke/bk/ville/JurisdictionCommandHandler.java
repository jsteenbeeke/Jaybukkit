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
package com.jeroensteenbeeke.bk.ville;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class JurisdictionCommandHandler extends AbstractVilleCommandHandler {
	public JurisdictionCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_USE);
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "jurisdiction".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		VillageLocation jurisdiction = null;
		int lastDistance = 0;

		List<VillageLocation> closestLocations = getClosestLocations(player
				.getLocation());
		for (VillageLocation loc : closestLocations) {
			if (jurisdiction == null) {
				jurisdiction = loc;
				lastDistance = distance(loc, player.getLocation());
			} else {
				int curDistance = distance(loc, player.getLocation());
				if (curDistance < lastDistance) {
					jurisdiction = loc;
					lastDistance = curDistance;
				}
			}
		}

		if (jurisdiction == null) {
			Messages.send(player, "This area is not within any jurisdiction");
		} else {

			Messages.send(
					player,
					String.format(
							"You are in the jurisdiction of &e%s&f, contact &e%s&f for more information",
							jurisdiction.getName(), jurisdiction.getOwner()));
			if (lastDistance >= (getVille().getMinimumDistance() / 2)) {
				Messages.send(
						player,

						"Also, it is possible that this area may become part of another jurisdiction in the future");
			}
		}

		return true;
	}
}
