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

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class WeatherCommandHandler extends PlayerAwareCommandHandler {

	public WeatherCommandHandler(JayOp jayOp) {
		super(jayOp.getServer(), JayOp.PERMISSION_ENVIRONMENT);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("weather").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).andArgumentEquals(0, "sun", "rain",
				"thunderstorm").itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if ("sun".equals(args[0])) {
			player.getWorld().setStorm(false);
			player.getWorld().setThundering(false);

			Messages.broadcast(player.getServer(), "Weather changed to &esun");
		}
		if ("rain".equals(args[0]) || "snow".equals(args[0])) {
			player.getWorld().setStorm(true);
			player.getWorld().setThundering(false);

			Messages.broadcast(player.getServer(),
					"Weather changed to &erain and snow");
		}
		if ("thunderstorm".equals(args[0])) {
			Messages.broadcast(player.getServer(),
					"Weather changed to &ethunderstorm");

			player.getWorld().setStorm(true);
			player.getWorld().setThundering(true);
		}
	}
}
