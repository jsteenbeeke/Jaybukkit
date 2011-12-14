package com.jeroensteenbeeke.bk.dbdump.tasks;

import com.jeroensteenbeeke.bk.dbdump.DatabaseDump;
import com.jeroensteenbeeke.bk.dbdump.DatabaseDumper;

public class DumpTask implements Runnable {
	private final DatabaseDump plugin;

	public DumpTask(DatabaseDump plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		DatabaseDumper dumper = new DatabaseDumper(plugin);

		dumper.dump(plugin.getDumpFolder());

	}

}
