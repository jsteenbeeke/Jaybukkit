package com.jeroensteenbeeke.bk.waypoint.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.waypoint.WaypointPlugin;
import com.jeroensteenbeeke.bk.waypoint.entities.Waypoint;

public class WaypointListHandler extends PlayerAwareCommandHandler {
	private final WaypointPlugin plugin;

	public WaypointListHandler(WaypointPlugin plugin) {
		super(plugin.getServer(), WaypointPlugin.USE_PERMISSION);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "wp-list".equals(command.getName()) && args.length == 0;
	}

	@Override
	public boolean onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			List<Waypoint> waypoints = plugin.getWaypoints()
					.getWaypointsByWorld(player.getWorld().getName());

			StringBuilder msg = new StringBuilder();
			msg.append("Waypoints in your world (&e" + waypoints.size()
					+ "&f): &a");

			int i = 0;
			for (Waypoint waypoint : waypoints) {
				if (i++ > 0) {
					msg.append("&f, &a");
				}

				msg.append(waypoint.getName());
			}

			Messages.send(player, msg.toString());

			return true;
		}
		return false;
	}

}
