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
package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;
import com.jeroensteenbeeke.bk.jayop.entities.Suspension;

public class SuspendCommandHandler extends PermissibleCommandHandler {
	private final JayOp plugin;

	public SuspendCommandHandler(JayOp plugin) {
		super(JayOp.PERMISSION_ENFORCEMENT);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("suspend").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).andArgumentIsValidPlayerName(0)
				.andArgumentLike(1, DECIMAL).itIsProper();
	}

	@Override
	public void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {

		OfflinePlayer player = plugin.getServer().getOfflinePlayer(
				args[0].toLowerCase());

		if (player != null) {
			for (Suspension suspension : plugin.getDatabase()
					.find(Suspension.class).where()
					.eq("playerName", player.getName()).findList()) {
				plugin.getDatabase().delete(suspension);
			}

			int days = Integer.parseInt(args[1]);

			Suspension suspension = new Suspension();
			suspension.setStart(System.currentTimeMillis());
			suspension.setDuration(1000 * 60 * 60 * 24 * days);
			suspension.setPlayerName(player.getName());
			plugin.getDatabase().save(suspension);

			Player p = plugin.getServer().getPlayerExact(player.getName());
			if (p != null) {
				p.kickPlayer("You have been suspended for " + days + " days");
			}

		} else {
			Messages.send(sender, "&cUnknown player &e" + args[0]);
		}

	}

}
