package com.jeroensteenbeeke.bk.ville;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.commands.AbstractVilleCommandHandler;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleCedeCommandHandler extends AbstractVilleCommandHandler {

	public VilleCedeCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_USE);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "cede").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 3) {
			String locName = args[1];
			String newOwner = args[2];

			VillageLocation location = getVille().getDatabase()
					.find(VillageLocation.class).where().eq("name", locName)
					.eq("owner", player.getName()).findUnique();

			if (location != null) {
				location.setOwner(newOwner);
				getVille().getDatabase().update(location);

				getLocationsHandle().remapJurisdictions();
			} else {
				Messages.send(
						player,
						String.format(
								"&cVillage location &e%s &c unknown or not owned by you",
								locName));
			}
		}

		return false;
	}

}
