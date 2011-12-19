package com.jeroensteenbeeke.bk.spleefregen.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class DespleefHandler extends PlayerAwareCommandHandler {

	private final SpleefRegen plugin;

	public DespleefHandler(SpleefRegen plugin) {
		super(plugin.getServer(), SpleefRegen.PERMISSION_SPLEEF_REMOVE);
		this.plugin = plugin;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("despleef").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(1).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		String name = args[0];

		SpleefPoint point = plugin.getDatabase().createQuery(SpleefPoint.class)
				.where().eq("name", name).findUnique();

		if (point != null) {
			World world = plugin.getServer().getWorld(point.getWorld());

			for (SpleefLocation loc : point.getLocations()) {
				Block b = world.getBlockAt(loc.getX(), loc.getY(), loc.getZ());
				b.setType(Material.AIR);

			}

			Messages.broadcast(plugin.getServer(), "&cThat's gotta hurt!");
		} else {
			Messages.send(player, "&cUnknown spleef location: &e" + args[0]);
		}

	}
}
