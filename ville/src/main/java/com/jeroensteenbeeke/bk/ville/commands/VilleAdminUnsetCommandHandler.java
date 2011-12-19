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

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleAdminUnsetCommandHandler extends AbstractVilleCommandHandler {

	public VilleAdminUnsetCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_ADMIN);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "admin").andArgIs(1, "unset")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(3).andArgumentEquals(0, "admin")
				.andArgumentEquals(1, "unset").andArgumentIsValidPlayerName(3)
				.itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String name = args[2];
		VillageLocation location = getVille().getDatabase()
				.find(VillageLocation.class).where().eq("name", name)
				.findUnique();

		if (location != null) {
			getVille().getDatabase().delete(location);

			getLocationsHandle().remapJurisdictions();

			Messages.send(player,
					String.format("Village location &e%s &fdeleted", name));
		}
	}
}
