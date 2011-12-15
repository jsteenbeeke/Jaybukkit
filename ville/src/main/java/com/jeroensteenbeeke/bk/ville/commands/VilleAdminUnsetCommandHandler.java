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
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleAdminUnsetCommandHandler extends PlayerAwareCommandHandler {
	private Ville ville;

	public VilleAdminUnsetCommandHandler(Ville ville) {
		super(ville.getServer(), Ville.PERMISSION_ADMIN);
		this.ville = ville;
	}

	@Override
	
	public CommandMatcher getMatcher() {
		return ifNameIs("ville-admin").andArgIs(0, "unset").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			String name = args[1];
			VillageLocation location = ville.getDatabase()
					.find(VillageLocation.class).where().eq("name", name)
					.findUnique();

			ville.getDatabase().delete(location);

			Messages.send(player,
					String.format("Village location &e%s &fdeleted", name));

			return true;
		}

		return false;
	}
}
