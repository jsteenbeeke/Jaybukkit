/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.dbdump;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.text.json.JsonContext;
import com.avaje.ebean.text.json.JsonWriteOptions;

public class DatabaseDumper {
	private Logger logger = Logger.getLogger("Minecraft");

	private DatabaseDump plugin;

	public DatabaseDumper(DatabaseDump plugin) {
		this.plugin = plugin;
	}

	public void restore(File dumpFolder) {
		for (JavaPlugin p : plugin.getPlugins()) {
			EbeanServer database = p.getDatabase();
			JsonContext json = database.createJsonContext();

			for (Class<?> c : p.getDatabaseClasses()) {
				processClass(dumpFolder, p, database, json, c);
			}
		}
	}

	public void dump(File dumpFolder) {

		for (JavaPlugin p : plugin.getPlugins()) {
			EbeanServer database = p.getDatabase();
			JsonContext json = database.createJsonContext();

			for (Class<?> c : p.getDatabaseClasses()) {
				String filename = createNewFilename(p, c);

				dumpToFile(dumpFolder, database, json, c, filename);
			}
		}
	}

	public void cleanup(File dumpFolder) {
		for (JavaPlugin p : plugin.getPlugins()) {
			for (Class<?> c : p.getDatabaseClasses()) {
				DumpFileFinder finder = new DumpFileFinder(p, c);
				String[] filenames = dumpFolder.list(finder);

				TreeMap<Long, String> timeToFile = new TreeMap<Long, String>();
				for (String filename : filenames) {
					timeToFile.put(finder.getDumpTime(filename), filename);
				}

				if (timeToFile.size() > plugin.getMaxDumps()) {
					int remove = timeToFile.size() - plugin.getMaxDumps();

					int i = 1;
					for (Entry<Long, String> e : timeToFile.entrySet()) {
						if (i++ > remove) {
							break;
						}

						File target = new File(dumpFolder, e.getValue());
						target.delete();
					}
				}

			}
		}
	}

	private void processClass(File dumpFolder, JavaPlugin p,
			EbeanServer database, JsonContext json, Class<?> c) {
		DumpFileFinder finder = new DumpFileFinder(p, c);

		String[] dumps = dumpFolder.list(finder);

		String found = determineLatestDumpFile(finder, dumps);

		if (found != null) {
			importFromFile(dumpFolder, database, json, c, found);

		} else {
			logger.info(String.format(
					"No dump available for table %s in plugin %s",
					c.getSimpleName(), p.getDescription().getName()));
		}
	}

	private String determineLatestDumpFile(DumpFileFinder finder, String[] dumps) {
		String found = null;
		long last = 0L;

		for (String dump : dumps) {
			long next = finder.getDumpTime(dump);
			if (next > last)
				found = dump;
		}
		return found;
	}

	private void importFromFile(File dumpFolder, EbeanServer database,
			JsonContext json, Class<?> c, String filename) {
		logger.info(String.format("Importing from dump file %s", filename));

		int i = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					dumpFolder, filename)));

			String next;

			while ((next = br.readLine()) != null) {
				Object o = json.toBean(c, next);
				database.save(o);
				i++;
			}

			logger.info(String.format("Imported %d objects", i));
		} catch (IOException e) {
			logger.severe("Failed to parse dump file: " + e.getMessage());
		}
	}

	private void dumpToFile(File dumpFolder, EbeanServer database,
			JsonContext json, Class<?> c, String filename) {
		File output = new File(dumpFolder, filename);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(output), true);

			List<?> objects = database.find(c).findList();
			JsonWriteOptions options = new JsonWriteOptions();
			options.setRootPathVisitor(new ForeignKeyVisitor());

			for (Object o : objects) {
				pw.println(json.toJsonString(o, false, options));
			}

			pw.close();

			logger.info(String.format("Wrote %d objects to %s", objects.size(),
					filename));
		} catch (IOException e) {
			logger.severe(String.format("Failed to open file: %s",
					e.getMessage()));
		}
	}

	private String createNewFilename(JavaPlugin p, Class<?> c) {
		StringBuilder builder = new StringBuilder();
		builder.append(p.getDescription().getName());
		builder.append('.');
		builder.append(c.getSimpleName());
		builder.append('.');
		builder.append(System.currentTimeMillis());
		builder.append(".dmp");

		String filename = builder.toString();
		return filename;
	}

	private static class DumpFileFinder implements FilenameFilter {
		private final String pattern;

		public DumpFileFinder(JavaPlugin plugin, Class<?> entityClass) {
			StringBuilder patternBuilder = new StringBuilder();
			patternBuilder.append(plugin.getDescription().getName());
			patternBuilder.append("\\.");
			patternBuilder.append(entityClass.getSimpleName());
			patternBuilder.append("\\.(\\d+)\\.dmp");

			pattern = patternBuilder.toString();
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.matches(pattern);
		}

		public long getDumpTime(String filename) {
			Matcher m = Pattern.compile(pattern).matcher(filename);
			if (m.matches()) {
				return Long.parseLong(m.group(1));
			}

			return 0L;
		}
	}

	public void restoreOnly(File dumpFolder, String name) {
		for (JavaPlugin p : plugin.getPlugins()) {
			if (name.equals(p.getDescription().getName())) {
				EbeanServer database = p.getDatabase();
				JsonContext json = database.createJsonContext();

				for (Class<?> c : p.getDatabaseClasses()) {
					processClass(dumpFolder, p, database, json, c);
				}
			}
		}
	}
}
