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

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.ville.AbstractVilleCommandHandler;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleClaimCommandHandler extends AbstractVilleCommandHandler {
	private Jayconomy jayconomy;

	public VilleClaimCommandHandler(Ville ville, Jayconomy jayconomy) {
		super(ville, Ville.PERMISSION_USE);

		this.jayconomy = jayconomy;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "claim").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			Location loc = player.getLocation();
			String name = args[1];

			List<VillageLocation> closestLocations = getClosestLocations(loc);
			if (closestLocations.size() == 0) {
				BigDecimal price = new BigDecimal(getVille().getPrice());

				boolean nameTaken = getVille().getDatabase()
						.find(VillageLocation.class).where().eq("name", name)
						.findRowCount() > 0;

				if (!nameTaken) {
					if (jayconomy.getBalance(player.getName()).compareTo(price) >= 0) {
						VillageLocation vl = new VillageLocation();
						vl.setName(args[1]);
						vl.setOwner(player.getName());
						vl.setWorld(loc.getWorld().getName());
						vl.setX(loc.getBlockX());
						vl.setY(loc.getBlockY());
						vl.setZ(loc.getBlockZ());

						getVille().getDatabase().save(vl);

						jayconomy.decreaseBalance(player.getName(), price);

						Messages.send(player, String.format(
								"Village location &e%s &fclaimed", name));
					} else {
						Messages.send(player, String.format(
								"&cYou require &e%s&c to claim this location",
								jayconomy.formatCurrency(price)));
					}
				} else {
					Messages.send(player, String.format(
							"&cThe name &e%s&c is already taken", name));
				}

			} else {
				Messages.send(player, "&cThis location is unsuitable");
				for (VillageLocation vl : closestLocations) {
					Messages.send(
							player,
							String.format("&e- &cToo close to &e%s",
									vl.getName()));
				}
			}

			return true;
		}

		return false;
	}
}
