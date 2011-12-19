package com.jeroensteenbeeke.bk.ville.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleAdminSetCommandHandler extends AbstractVilleCommandHandler {
	public VilleAdminSetCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_ADMIN);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "admin").andArgIs(1, "set")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(4).andArgumentEquals(0, "admin")
				.andArgumentEquals(1, "set").andArgumentIsValidPlayerName(3)
				.itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String name = args[2];
		String owner = args[3];

		boolean nameTaken = getVille().getDatabase()
				.find(VillageLocation.class).where().eq("name", name)
				.findRowCount() > 0;

		if (!nameTaken) {
			Location loc = player.getLocation();

			VillageLocation vl = new VillageLocation();
			vl.setName(name);
			vl.setOwner(owner);
			vl.setWorld(loc.getWorld().getName());
			vl.setX(loc.getBlockX());
			vl.setY(loc.getBlockY());
			vl.setZ(loc.getBlockZ());
			getVille().getDatabase().save(vl);

			getLocationsHandle().remapJurisdictions();

			Messages.send(player,
					String.format("Village location &e%s &fset", name));
		} else {
			Messages.send(player,
					String.format("&cThe name &e%s&c is already taken", name));
		}

	}
}
