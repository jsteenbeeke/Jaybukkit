package com.jeroensteenbeeke.bk.basics.commands;

import org.bukkit.command.Command;

public interface CommandMatcher {
	boolean matches(Command command, String[] args);
}
