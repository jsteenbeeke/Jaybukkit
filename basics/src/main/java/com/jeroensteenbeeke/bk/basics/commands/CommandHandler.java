package com.jeroensteenbeeke.bk.basics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandHandler
{
	boolean matches(Command command, String[] args);

	boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
