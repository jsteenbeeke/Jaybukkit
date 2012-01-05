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
package com.jeroensteenbeeke.bk.firestopper.listeners;

import java.awt.Point;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;

import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.firestopper.Firestopper;

public class FirestopperListener extends BlockListener {
	private final int burnIfUnauthorized;

	private static final BlockFace[] FACES = { BlockFace.EAST, BlockFace.SOUTH,
			BlockFace.WEST, BlockFace.NORTH };

	private static final Set<Point> CORNERS = Sets.newHashSet(new Point(0, 0),
			new Point(0, 4), new Point(3, 0), new Point(3, 4));

	public FirestopperListener(int burnIfUnauthorized) {
		this.burnIfUnauthorized = burnIfUnauthorized;
	}

	@Override
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.isCancelled())
			return;

		if (event.getCause().equals(
				BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL)) {
			if (event.getBlock().getType() == Material.OBSIDIAN
					|| event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
				Block block = (event.getBlock().getType() == Material.OBSIDIAN) ? event
						.getBlock() : event.getBlock().getRelative(
						BlockFace.DOWN);

				// Check if we're trying to light an obsidian portal
				boolean found = false;
				for (BlockFace face : FACES) {
					found = checkFaceForPortal(face, block);
					if (found)
						break;
				}

				if (!found) {
					performAuthorizedBurnCheck(event);
				} else {
					Messages.send(event.getPlayer(), "&aPortal granted");
				}

			} else {

				performAuthorizedBurnCheck(event);
			}
		} else {
			event.setCancelled(true);
		}
	}

	private boolean checkFaceForPortal(BlockFace face, Block block) {
		Material[][] rows = new Material[4][5];
		Material[][] desired = new Material[4][5];

		rows = populate(rows, 1, 0, block, face);
		desired = fillDesired(desired);

		boolean matches = matches(rows, desired);

		if (!matches) {
			desired[1][1] = Material.FIRE;

			matches = matches(rows, desired);
		}

		return matches;
	}

	private boolean matches(Material[][] rows, Material[][] desired) {
		for (int x = 0; x <= 3; x++) {
			for (int y = 0; y <= 4; y++) {
				if (desired[x][y] != null) { // Null == don't care
					if (rows[x][y] != desired[x][y]) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private Material[][] fillDesired(Material[][] desired) {

		for (int x = 0; x <= 3; x++) {
			for (int y = 0; y <= 4; y++) {
				Point p = new Point(x, y);
				if (!CORNERS.contains(p)) {
					if (x == 0 || x == 3 || y == 0 || y == 4) {
						desired[x][y] = Material.OBSIDIAN;
					} else {
						desired[x][y] = Material.AIR;
					}
				} else {
					desired[x][y] = null;
				}
			}
		}

		return desired;
	}

	private Material[][] populate(Material[][] rows, int x, int y, Block block,
			BlockFace face) {
		if (x >= 0 && x <= 3) {
			if (y >= 0 && y <= 4) {
				if (rows[x][y] == null) {
					rows[x][y] = block.getType();

					return populate(
							populate(
									populate(rows, x, y + 1,
											block.getRelative(BlockFace.UP),
											face), x + 1, y,
									block.getRelative(face.getOppositeFace()),
									face), x - 1, y, block.getRelative(face),
							face);
				}
			}
		}

		return rows;
	}

	private void performAuthorizedBurnCheck(BlockIgniteEvent event) {
		event.setCancelled(event.getPlayer() == null
				|| !event.getPlayer().hasPermission(
						Firestopper.PERMISSION_START_FIRE));

		if (event.getPlayer() != null
				&& !event.getPlayer().hasPermission(
						Firestopper.PERMISSION_START_FIRE)) {
			if (burnIfUnauthorized > 0) {
				event.getPlayer().setFireTicks(burnIfUnauthorized);
			}
			Messages.send(event.getPlayer(),
					"&cYou are not authorized to start fires");
		}
	}
}
