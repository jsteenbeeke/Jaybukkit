package com.jeroensteenbeeke.bk.ville.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleAdminSetCommandHandler extends PlayerAwareCommandHandler {
	private final Ville ville;

	public VilleAdminSetCommandHandler(Ville ville) {
		super(ville.getServer(), Ville.PERMISSION_ADMIN);
		this.ville = ville;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville-admin").andArgIs(0, "set").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 3) {
			String name = args[1];
			String owner = args[2];

			boolean nameTaken = ville.getDatabase().find(VillageLocation.class)
					.where().eq("name", name).findRowCount() > 0;

			if (!nameTaken) {
				Location loc = player.getLocation();

				VillageLocation vl = new VillageLocation();
				vl.setName(name);
				vl.setOwner(owner);
				vl.setWorld(loc.getWorld().getName());
				vl.setX(loc.getBlockX());
				vl.setY(loc.getBlockY());
				vl.setZ(loc.getBlockZ());

				ville.getDatabase().save(vl);

				Messages.send(player,
						String.format("Village location &e%s &fset", name));
			} else {
				Messages.send(player, String.format(
						"&cThe name &e%s&c is already taken", name));
			}

			return true;
		}

		return false;
	}

}
