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
package com.jeroensteenbeeke.bk.blockhistory.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.blockhistory.BlockHistory;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange;

public class GetHistoryCommand extends PlayerAwareCommandHandler {
	private final BlockHistory plugin;

	private static final DateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss z");

	private static final HashSet<Byte> transparent = new HashSet<Byte>();

	static {
		transparent.add(new Integer(Material.AIR.getId()).byteValue());
	}

	public GetHistoryCommand(BlockHistory plugin) {
		super(plugin.getServer(), BlockHistory.PERMISSION_VIEW_HISTORY);

		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "history".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 0 || args.length == 1) {
			Block block = player.getTargetBlock(transparent, 100);

			List<BlockChange> history = new ArrayList<BlockChange>(5);

			if (args.length == 0) {
				for (BlockChange b : plugin.getBaseQuery(block).findList()) {
					history.add(0, b);
				}
			} else {
				try {
					int page = Integer.parseInt(args[1]);

					for (BlockChange b : plugin.getBaseQuery(block)
							.setFirstRow(page * 5).findList()) {
						history.add(0, b);
					}
				} catch (NumberFormatException nfe) {
					Messages.send(player, "&cInvalid page: &e" + args[1]);
					return true;
				}
			}

			if (history.isEmpty()) {
				if (args.length == 0) {
					Messages.send(player, "&cNo history for this block");
				} else {
					Messages.send(player, "&cInvalid page: &e" + args[1]);
				}
			} else {
				Messages.send(player, String.format(
						"&aHistory for (&e%s&a, &e%s&a, &e%s&a)", block.getX(),
						block.getY(), block.getZ()));
				for (BlockChange change : history) {
					String culpritString = change.getCulprit() == null ? ""
							: "by " + change.getCulprit();

					Messages.send(player, String.format(
							"  &e- %s&a: &e%s &ato &e%s %s",
							FORMAT.format(change.getChangeDate()), change
									.getChangeType().name().toLowerCase(),
							Material.getMaterial(change.getBlockType()).name(),
							culpritString));
				}
			}

			return true;
		}
		return false;
	}

}
