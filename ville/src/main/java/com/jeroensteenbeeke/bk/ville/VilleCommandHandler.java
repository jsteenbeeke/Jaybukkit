package com.jeroensteenbeeke.bk.ville;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleCommandHandler extends AbstractVilleCommandHandler {
	private Jayconomy jayconomy;

	public VilleCommandHandler(Ville ville, Jayconomy jayconomy) {
		super(ville, Ville.PERMISSION_USE);

		this.jayconomy = jayconomy;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "ville".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			if ("claim".equals(args[0])) {
				Location loc = player.getLocation();
				String name = args[1];

				List<VillageLocation> closestLocations = getClosestLocations(loc);
				if (closestLocations.size() == 0) {
					BigDecimal price = new BigDecimal(getVille().getPrice());

					boolean nameTaken = getVille().getDatabase()
							.find(VillageLocation.class).where()
							.eq("name", name).findRowCount() > 0;

					if (!nameTaken) {
						if (jayconomy.getBalance(player.getName()).compareTo(
								price) >= 0) {
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
							Messages.send(
									player,
									String.format(
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
			} else if ("unclaim".equals(args[0])) {
				String name = args[1];
				VillageLocation location = getVille().getDatabase()
						.find(VillageLocation.class).where().eq("name", name)
						.eq("owner", player.getName()).findUnique();

				if (location != null) {

					getVille().getDatabase().delete(location);

					Messages.send(player, String.format(
							"Village location &e%s &fdeleted", name));
				} else {
					Messages.send(
							player,
							String.format(
									"&cVillage location &e%s &c unknown or not owned by you",
									name));
				}

				return true;
			}
		} else if (args.length == 1) {
			if ("check".equals(args[0])) {
				List<VillageLocation> closestLocations = getClosestLocations(player
						.getLocation());
				if (closestLocations.size() == 0) {
					Messages.send(player, "&aThis location is suitable");
				} else {
					Messages.send(player, "&cThis location is unsuitable");
					for (VillageLocation loc : closestLocations) {
						Messages.send(
								player,
								String.format("&e- &cToo close to &e%s",
										loc.getName()));
					}
				}

				return true;
			}
		}

		Messages.send(player, "/ville claim <name>");
		Messages.send(player, "/ville unclaim <name>");
		Messages.send(player, "/ville check");

		return true;
	}
}
