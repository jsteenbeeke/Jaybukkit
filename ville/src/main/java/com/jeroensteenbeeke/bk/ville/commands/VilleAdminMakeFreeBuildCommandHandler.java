package com.jeroensteenbeeke.bk.ville.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleAdminMakeFreeBuildCommandHandler extends
		AbstractVilleCommandHandler {
	public VilleAdminMakeFreeBuildCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_ADMIN);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "admin").andArgIs(1, "freebuild")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(3).andArgumentEquals(0, "admin")
				.andArgumentEquals(1, "freebuild").itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String name = args[2];

		VillageLocation loc = getVille().getDatabase()
				.find(VillageLocation.class).where().eq("name", name)
				.findUnique();

		loc.setEntryLevel(true);

		getVille().getDatabase().update(loc);

		getLocationsHandle().remapJurisdictions();

	}
}
