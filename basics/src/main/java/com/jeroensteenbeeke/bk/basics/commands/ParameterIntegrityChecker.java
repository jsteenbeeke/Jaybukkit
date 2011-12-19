package com.jeroensteenbeeke.bk.basics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ParameterIntegrityChecker {
	boolean isProperlyInvoked(CommandSender sender, Command command,
			String label, String[] args);
}
