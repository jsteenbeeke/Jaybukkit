package com.jeroensteenbeeke.bk.ville;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleAdminCommandHandler extends PlayerAwareCommandHandler {
	private Ville ville;

	public VilleAdminCommandHandler(Ville ville) {
		super(ville.getServer(), Ville.PERMISSION_ADMIN);
		this.ville = ville;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "ville-admin".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 3) {
			if ("set".equals(args[0])) {
				String name = args[1];
				String owner = args[2];

				boolean nameTaken = ville.getDatabase()
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

					ville.getDatabase().save(vl);

					Messages.send(player,
							String.format("Village location &e%s &fset", name));
				} else {
					Messages.send(player, String.format(
							"&cThe name &e%s&c is already taken", name));
				}

				return true;
			}
		} else if (args.length == 2) {
			if ("unset".equals(args[0])) {
				String name = args[1];
				VillageLocation location = ville.getDatabase()
						.find(VillageLocation.class).where().eq("name", name)
						.findUnique();

				ville.getDatabase().delete(location);

				Messages.send(player,
						String.format("Village location &e%s &fdeleted", name));

				return true;
			}
		}

		Messages.send(player, "/ville-admin set <name> <owner>");
		Messages.send(player, "/ville-admin unset <name>");

		return true;
	}
}
