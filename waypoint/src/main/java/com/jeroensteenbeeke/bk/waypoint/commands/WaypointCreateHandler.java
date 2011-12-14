package com.jeroensteenbeeke.bk.waypoint.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;

public class WaypointCreateHandler implements CommandHandler {
	private final WaypointPlugin plugin;

	public WaypointCreateHandler(WaypointPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "wp-create".equals(command.getName()) && args.length == 1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(WaypointPlugin.CREATE_PERMISSION)) {
			String name = args[0];

			if (plugin.getWaypoints().getWaypoint(name) == null) {

				Player player = plugin.getServer().getPlayerExact(
						sender.getName());

				int lx = player.getLocation().getBlockX();
				int y = player.getLocation().getBlockY();
				int lz = player.getLocation().getBlockZ();

				player.sendRawMessage("Scanning location (\u00A7e" + lx
						+ "\u00A7f,\u00A7e" + y + "\u00A7f,\u00A7e" + lz
						+ "\u00A7f) for waypoint suitability");

				if (y > player.getWorld().getMaxHeight() - 2) {
					player.sendRawMessage("\u00A7cLocation too high for waypoint\u00A7f");
					return true;
				}

				int i = -2;

				for (int x = lx - 2; x <= lx + 2; x++) {
					int j = -2;

					for (int z = lz - 2; z <= lz + 2; z++) {
						if (Math.abs(i) != 2 || Math.abs(j) != 2) {
							if (plugin.getWaypoints().isWaypoint(x, y, z)) {
								player.sendRawMessage("\u00A7cUnsuitable location, already contains waypoint\u00A7f");
								return true;
							}

							Block mustBeAir = player.getWorld().getBlockAt(x,
									y, z);
							Block mustBeSolid = player.getWorld().getBlockAt(x,
									y - 1, z);

							if (!mustBeAir.isEmpty()
									&& mustBeAir.getType() != Material.SNOW) {
								player.sendRawMessage("\u00A7cUnsuitable location, area not clear\u00A7f");
								player.sendRawMessage("\u00A7cLocation (\u00A7e"
										+ x
										+ "\u00A7c,\u00A7e"
										+ y
										+ "\u00A7c,\u00A7e"
										+ z
										+ "\u00A7c) is "
										+ mustBeAir.getType().toString());
								return true;
							}

							if (mustBeSolid.isEmpty() || mustBeSolid.isLiquid()) {
								player.sendRawMessage("\u00A7cUnsuitable location, area not on top of solid blocks\u00A7f");
								if (mustBeSolid.isEmpty()) {

									player.sendRawMessage("\u00A7cLocation (\u00A7e"
											+ x
											+ "\u00A7c,\u00A7e"
											+ y
											+ "\u00A7c,\u00A7e"
											+ z
											+ "\u00A7c) is air");
								} else if (mustBeSolid.isLiquid()) {
									player.sendRawMessage("\u00A7cLocation (\u00A7e"
											+ x
											+ "\u00A7c,\u00A7e"
											+ y
											+ "\u00A7c,\u00A7e"
											+ z
											+ "\u00A7c) is liquid");
								}
								return true;
							}

						}

						j++;
					}

					i++;
				}

				// Location suitable

				i = -2;

				for (int x = lx - 2; x <= lx + 2; x++) {
					int j = -2;

					for (int z = lz - 2; z <= lz + 2; z++) {
						if (Math.abs(i) != 2 || Math.abs(j) != 2) {
							Block b = player.getWorld().getBlockAt(x, y, z);
							if (Math.abs(i) == 2 || Math.abs(j) == 2) {
								// Outer layer, single slab
								b.setTypeId(44);
							} else if (Math.abs(i) == 1 || Math.abs(j) == 1) {
								// Inner layer, double slab
								b.setTypeId(43);
							} else {
								// Center, lightstone
								b.setType(Material.GLOWSTONE);
							}

						}

						j++;
					}

					i++;
				}

				Location loc = new Location(player.getWorld(), lx, y, lz);

				plugin.getWaypoints().newWaypoint(loc, name);
				player.teleport(loc.add(0, 2, 0));

				player.sendRawMessage("Waypoint \u00A7a" + name
						+ "\u00A7f created");
				return true;
			} else {
				sender.sendMessage("\u00A7cWaypoint \u00A7a" + name
						+ "\u00A7c already exists\u00A7f");
				return true;
			}
		} else {
			sender.sendMessage("\u00A7cYou do not have permission to create Waypoints\u00A7f");
			return true;
		}
	}
}
