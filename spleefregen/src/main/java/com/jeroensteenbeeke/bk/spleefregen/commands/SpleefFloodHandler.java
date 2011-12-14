package com.jeroensteenbeeke.bk.spleefregen.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;

public class SpleefFloodHandler extends PlayerAwareCommandHandler {
	private SpleefRegen plugin;

	public SpleefFloodHandler(SpleefRegen regen) {
		super(regen.getServer(), SpleefRegen.PERMISSION_CREATE_SPLEEF);
		this.plugin = regen;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "splood".matches(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			Location loc = player.getLocation();

			List<SpleefLocation> blocks = plugin.getDatabase()
					.createQuery(SpleefLocation.class).where()
					.eq("x", loc.getBlockX()).ge("y", loc.getBlockY())
					.eq("z", loc.getBlockZ()).findList();

			for (SpleefLocation spl : blocks) {
				if (spl.getPoint().getWorld().equals(loc.getWorld().getName())) {
					List<Location> locations = plugin.compileLocations(
							player.getWorld(), player.getLocation());

					if (locations.isEmpty()) {
						Messages.send(player,
								"&cUnsuitable location, or area too large");
						return true;
					} else {
						Messages.send(player, "&aTeleporting you to safety");

						player.teleport(new Location(loc.getWorld(),
								spl.getX(), spl.getY() + 3, spl.getZ()));

						for (Location l : locations) {
							l.getBlock().setType(Material.LAVA);
						}

						Messages.send(player, "&aFlood complete");

						return true;
					}
				}
			}

			Messages.send(player, "&cYou are not underneath a spleef area");

			return true;
		}

		return false;
	}

}
