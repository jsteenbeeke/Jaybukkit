package com.jeroensteenbeeke.bk.dbdump.tasks;

import com.jeroensteenbeeke.bk.dbdump.DatabaseDump;
import com.jeroensteenbeeke.bk.dbdump.DatabaseDumper;

public class CleanupTask implements Runnable {
	private final DatabaseDump plugin;

	public CleanupTask(DatabaseDump plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		DatabaseDumper dumper = new DatabaseDumper(plugin);

		dumper.cleanup(plugin.getDumpFolder());

	}

}
