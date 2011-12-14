package com.jeroensteenbeeke.bk.spleefregen.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefCreateHandler extends PlayerAwareCommandHandler {

	private final SpleefRegen plugin;

	public SpleefCreateHandler(SpleefRegen plugin) {
		super(plugin.getServer(), SpleefRegen.PERMISSION_CREATE_SPLEEF);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "spleefgen".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

		if (args.length == 2) {
			if (player != null) {
				List<Location> locations = plugin.compileLocations(
						player.getWorld(), player.getLocation());

				if (locations.isEmpty()) {
					Messages.send(player,
							"&cUnsuitable location, or area too large");
					return true;
				} else {
					String name = args[0];
					int total = plugin.getDatabase()
							.createQuery(SpleefPoint.class).where()
							.eq("name", name).findRowCount();

					if (total == 0) {
						int type = 0;
						if ("dirt".equals(args[1])) {
							type = 3;
						} else if ("snow".equals(args[1])) {
							type = 80;
						} else if ("wool".equals(args[1])) {
							type = 35;
						} else if ("nether".equals(args[1])) {
							type = 83;
						} else {

							Messages.send(
									player,
									"&cInvalid material: &e"
											+ args[1]
											+ "&c, only the following are allowed: &edirt&c, &esnow&c, &ewool&c, &enether");
							return true;
						}

						player.teleport(player.getLocation().add(0.0, 2.0, 0.0));

						SpleefPoint point = new SpleefPoint();
						point.setName(name);
						point.setMaterial(type);
						point.setWorld(player.getWorld().getName());
						point.setLocked(true);
						plugin.getDatabase().save(point);

						for (Location location : locations) {
							SpleefLocation loc = new SpleefLocation();
							loc.setPoint(point);
							loc.setX(location.getBlockX());
							loc.setY(location.getBlockY());
							loc.setZ(location.getBlockZ());
							plugin.getDatabase().save(loc);
							if (location.getBlock().getTypeId() != type) {
								location.getBlock().setTypeId(type);
							}
						}

						return true;

					} else {
						Messages.send(player, "&cName taken");
						return true;
					}
				}
			} else {
				Messages.send(player,
						"&cYou do not have the capability to create spleef areas");
				return true;
			}
		}

		return false;
	}

}
