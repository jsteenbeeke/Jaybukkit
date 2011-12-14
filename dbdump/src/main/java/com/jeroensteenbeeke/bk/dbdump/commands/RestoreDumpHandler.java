package com.jeroensteenbeeke.bk.dbdump.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.dbdump.DatabaseDump;
import com.jeroensteenbeeke.bk.dbdump.DatabaseDumper;

public class RestoreDumpHandler extends PermissibleCommandHandler {
	private final DatabaseDump plugin;

	public RestoreDumpHandler(DatabaseDump plugin) {
		super(DatabaseDump.PERMISSION_RESTORE);
		this.plugin = plugin;
	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "dbrestore".matches(command.getName());
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		DatabaseDumper dumper = new DatabaseDumper(plugin);

		dumper.restore(plugin.getDumpFolder());

		return true;
	}

}
