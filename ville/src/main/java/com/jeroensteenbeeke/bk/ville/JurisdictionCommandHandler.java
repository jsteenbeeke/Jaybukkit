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
