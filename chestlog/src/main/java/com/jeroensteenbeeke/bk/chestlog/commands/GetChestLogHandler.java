/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.chestlog.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.chestlog.ChestLogPlugin;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestData;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLocation;
import com.jeroensteenbeeke.bk.chestlog.entities.ChestLog;

public class GetChestLogHandler extends PlayerAwareCommandHandler {
	private final ChestLogPlugin plugin;

	private static final DateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss z");

	private static final HashSet<Byte> transparent = new HashSet<Byte>();

	static {
		transparent.add(new Integer(Material.AIR.getId()).byteValue());
	}

	public GetChestLogHandler(ChestLogPlugin plugin) {
		super(plugin.getServer(), ChestLogPlugin.PERMISSION_CHESTLOG_VIEW);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("chestlog").itMatches();
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 0 || args.length == 1) {
			Block block = player.getTargetBlock(transparent, 100);

			if (block != null) {

				Location loc = block.getLocation();

				ChestLocation location = plugin.getDatabase()
						.createQuery(ChestLocation.class).where()
						.eq("x", loc.getBlockX()).eq("y", loc.getBlockY())
						.eq("z", loc.getBlockZ())
						.eq("world", loc.getWorld().getName()).findUnique();

				if (location != null) {
					ChestData chest = location.getChest();

					int logsize = chest.getLogs().size();

					if (logsize > 7) {
						int pages = 1 + (logsize / 7);

						List<ChestLog> logs = new ArrayList<ChestLog>(7);
						try {
							int page = (args.length == 0) ? 1 : Integer
									.parseInt(args[0]);

							if (page > pages || page < 1) {
								Messages.send(player, "&cInvalid page: &e"
										+ page);
							} else {

								int last = page * 7 >= logsize ? logsize
										: page * 7;

								logs.addAll(chest.getLogs().subList(
										(page - 1) * 7, last));

								sendLogs(player, logs);
							}

						} catch (NumberFormatException nfe) {
							Messages.send(player, "&cInvalid page: &e"
									+ args[0]);
						}

					} else {
						sendLogs(player, chest.getLogs());
					}

				} else {
					Messages.send(player,
							"&cYou do not seem to be looking at a known chest");
				}
			} else {
				Messages.send(player,
						"&cYou do not seem to be looking at anything");
			}

			return true;
		}

		return false;
	}

	private void sendLogs(Player player, List<ChestLog> logs) {
		Messages.send(player, "&2Chest access log: ");

		for (ChestLog log : logs) {
			Messages.send(player,
					"  &2- " + " &f[&c" + log.getPlayer() + "&f]&2 @ &e"
							+ format(log.getDate()) + "&2: " + log.getMessage());
		}

	}

	private String format(Date date) {

		return FORMAT.format(date);
	}

}
