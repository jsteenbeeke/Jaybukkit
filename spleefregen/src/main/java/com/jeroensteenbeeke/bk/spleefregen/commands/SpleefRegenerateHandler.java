package com.jeroensteenbeeke.bk.spleefregen.commands;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.spleefregen.SpleefRegen;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefLocation;
import com.jeroensteenbeeke.bk.spleefregen.entities.SpleefPoint;

public class SpleefRegenerateHandler implements CommandHandler {

	private final SpleefRegen plugin;

	public SpleefRegenerateHandler(SpleefRegen plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "respleef".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender.hasPermission(SpleefRegen.PERMISSION_REGENERATE_SPLEEF)) {
			if (args.length == 1) {
				String name = args[0];

				SpleefPoint point = plugin.getDatabase()
						.createQuery(SpleefPoint.class).where()
						.eq("name", name).findUnique();

				if (point != null) {
					World world = plugin.getServer().getWorld(point.getWorld());
					for (SpleefLocation loc : point.getLocations()) {
						Block block = world.getBlockAt(loc.getX(), loc.getY(),
								loc.getZ());
						block.setTypeId(point.getMaterial());
					}
					return true;
				} else {
					Messages.send(sender, "&cUnknown spleef location: &e"
							+ args[0]);
					return true;
				}

			}
		} else {
			Messages.send(sender,
					"&cYou do not have permission to regenerate spleef areas");
			return true;
		}

		return false;
	}
}
