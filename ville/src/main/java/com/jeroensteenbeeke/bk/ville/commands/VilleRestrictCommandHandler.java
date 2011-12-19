package com.jeroensteenbeeke.bk.ville.commands;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleRestrictCommandHandler extends AbstractVilleCommandHandler {
	private Jayconomy jayconomy;

	public VilleRestrictCommandHandler(Ville ville, Jayconomy jayconomy) {
		super(ville, Ville.PERMISSION_USE);
		this.jayconomy = jayconomy;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "restrict").itMatches();
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
			if (!location.isRestricted()) {
				BigDecimal price = new BigDecimal(getVille().getRestrictPrice());

				if (jayconomy.getBalance(player.getName()).compareTo(price) >= 0) {
					location.setRestricted(true);
					getVille().getDatabase().update(location);

					Messages.broadcast(getVille().getServer(), String.format(
							"&cLocation &e%s&c is now restricted", locName));

				} else {
					Messages.send(player, String.format(
							"&cYou require &e%s&c to claim this location",
							jayconomy.formatCurrency(price)));
				}
			} else {
				Messages.send(player, String.format(
						"&cVillage location &e%s &c is already restricted",
						locName));
			}
		} else {
			Messages.send(player, String.format(
					"&cVillage location &e%s &c unknown or not owned by you",
					locName));
		}
	}

}
