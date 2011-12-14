package com.jeroensteenbeeke.bk.jayop.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class UnbanCommandHandler implements CommandHandler
{
	private final JayOp plugin;

	public UnbanCommandHandler(JayOp plugin)
	{
		super();
		this.plugin = plugin;
	}

	public JayOp getPlugin()
	{
		return plugin;
	}

	@Override
	public boolean matches(Command command, String[] args)
	{
		return "unban".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender.hasPermission(JayOp.PERMISSION_ENFORCEMENT))
		{
			if (args.length == 1)
			{
				OfflinePlayer target = sender.getServer().getOfflinePlayer(args[0]);

				if (target != null)
				{
					target.setBanned(false);

					sender.getServer().broadcastMessage(
						"\u00A7cPlayer \u00A7e" + args[0] + "\u00A7c has been unbanned\u00A7f");

					return true;
				}
				else
				{
					sender.sendMessage("\u00A7cUnknown player: " + args[0] + "\u00A7f");

					return true;
				}

			}
		}
		else
		{
			sender.sendMessage("\u00A7cYou do not have permission to ban people\u00A7f");

			return true;
		}

		return false;
	}

}
