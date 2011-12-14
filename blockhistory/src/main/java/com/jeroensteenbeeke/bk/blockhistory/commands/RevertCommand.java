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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.blockhistory.BlockHistory;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange;
import com.jeroensteenbeeke.bk.blockhistory.entities.BlockChange.BlockChangeType;

public class RevertCommand extends PlayerAwareCommandHandler {
	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private BlockHistory history;

	public RevertCommand(BlockHistory history) {
		super(history.getServer(), BlockHistory.PERMISSION_REVERT_HISTORY);
		this.history = history;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "revert".equals(command.getName());
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 2) {
			String playerName = args[0];
			try {
				Date date = FORMAT.parse(args[1]);

				List<BlockChange> changes = history.getDatabase()
						.find(BlockChange.class).where()
						.eq("culprit", playerName).ge("changeDate", date)
						.order().desc("changeDate").findList();

				for (BlockChange change : changes) {
					if (change.getOverrides() != null) {
						World world = history.getServer().getWorld(
								change.getWorld());

						Block b = world.getBlockAt(change.getX(),
								change.getY(), change.getZ());

						Material m = Material.getMaterial(change.getOverrides()
								.getBlockType());

						if (b.getType() == Material.AIR) {
							if (m != Material.AIR) {
								history.addBlock(b.getX(), b.getY(), b.getZ(),
										m.getId(), b.getWorld().getName(),
										player.getName(),
										BlockChangeType.REVERTED);
							}
						} else {
							if (m != b.getType()) {
								if (m == Material.AIR) {
									history.removeBlock(b.getX(), b.getY(), b
											.getZ(), b.getTypeId(), b
											.getWorld().getName(), player
											.getName(),
											BlockChangeType.REVERTED);
								} else {
									history.removeBlock(b.getX(), b.getY(), b
											.getZ(), b.getTypeId(), b
											.getWorld().getName(), player
											.getName(),
											BlockChangeType.REVERTED);
									history.addBlock(b.getX(), b.getY(), b
											.getZ(), m.getId(), b.getWorld()
											.getName(), player.getName(),
											BlockChangeType.REVERTED);

								}
							}
						}

						b.setType(m);

						Messages.send(
								player,
								String.format(
										"&aReverted &a(&e%s&a,&e%s&a,&e%s&a) in &e%s&a to &e%s",
										change.getX(), change.getY(),
										change.getZ(), change.getWorld(),
										m.name()));
					}
				}

				Messages.send(player, "&aRevert complete");

			} catch (ParseException e) {
				Messages.send(player, "&cInvalid date. Format is yyyy-mm-dd");
			}

			return true;
		}

		return false;
	}

}
