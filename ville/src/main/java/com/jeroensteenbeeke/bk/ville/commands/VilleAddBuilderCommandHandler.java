package com.jeroensteenbeeke.bk.ville.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;
import com.jeroensteenbeeke.bk.ville.entities.VilleBuilder;

public class VilleAddBuilderCommandHandler extends AbstractVilleCommandHandler
		implements CommandHandler {
	public VilleAddBuilderCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_USE);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "builder").andArgIs(1, "add")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(4).andArgumentIsValidPlayerName(3).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String locName = args[2];
		String targetPlayer = args[3];

		VillageLocation location = getVille().getDatabase()
				.find(VillageLocation.class).where().eq("name", locName)
				.eq("owner", player.getName()).findUnique();

		if (location != null) {
			if (location.isRestricted()) {
				VilleBuilder builder = getVille().getDatabase()
						.find(VilleBuilder.class).where()
						.eq("location", location).eq("player", targetPlayer)
						.findUnique();

				if (builder == null) {
					builder = new VilleBuilder();
					builder.setLocation(location);
					builder.setPlayer(targetPlayer);
					getVille().getDatabase().save(builder);

					Messages.send(player, String.format(
							"Player &e%s&f added as builder for &e%s",
							targetPlayer, locName));

					getLocationsHandle().addBuilder(location, targetPlayer);
				} else {
					Messages.send(
							player,
							String.format(
									"&cPlayer &e%s&c already registered as builder for &e%s",
									targetPlayer, locName));
				}
			} else {
				Messages.send(
						player,
						String.format(
								"&cLocation &e%s&c is not a restricted location. Use /ville restrict <location>",
								locName));
			}
		} else {
			Messages.send(player, String.format(
					"&cVillage location &e%s &c unknown or not owned by you",
					locName));
		}

	}
}
