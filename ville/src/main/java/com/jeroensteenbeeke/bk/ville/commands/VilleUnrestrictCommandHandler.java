package com.jeroensteenbeeke.bk.ville.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleUnrestrictCommandHandler extends AbstractVilleCommandHandler {

	public VilleUnrestrictCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_USE);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "unrestrict").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {

		return ifArgCountIs(2).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String locName = args[1];

		VillageLocation location = getVille().getDatabase()
				.find(VillageLocation.class).where().eq("name", locName)
				.eq("owner", player.getName()).findUnique();

		if (location != null) {
			if (location.isRestricted()) {
				location.setRestricted(true);
				getVille().getDatabase().update(location);

				Messages.broadcast(getVille().getServer(), String.format(
						"&cLocation &e%s&c is now restricted", locName));

			} else {
				Messages.send(player, String
						.format("&cVillage location &e%s &c is not restricted",
								locName));
			}
		} else {
			Messages.send(player, String.format(
					"&cVillage location &e%s &c unknown or not owned by you",
					locName));
		}

	}

}
