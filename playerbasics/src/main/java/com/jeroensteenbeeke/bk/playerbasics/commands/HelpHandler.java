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
package com.jeroensteenbeeke.bk.playerbasics.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.MapOp;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.playerbasics.PlayerBasics;

public class HelpHandler extends PermissibleCommandHandler {
	private static final String KEY_PERMISSIONS = "permission";
	private static final String KEY_USAGE = "usage";
	private static final String KEY_DESCRIPTION = "description";

	private static class CommandData implements Serializable {
		private static final long serialVersionUID = 1L;

		private final String name;
		private final String description;
		private final String permissions;
		private final String usage;
		private final String plugin;

		public CommandData(String name, String description, String permissions,
				String usage, String plugin) {
			super();
			this.name = name;
			this.description = description;
			this.permissions = permissions;
			this.usage = usage;
			this.plugin = plugin;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getPermissions() {
			return permissions;
		}

		public String getUsage() {
			return usage;
		}

		public String getPlugin() {
			return plugin;
		}

	}

	private Map<String, List<CommandData>> permissionsToCommandMap = null;

	private Map<String, List<CommandData>> nameToCommandMap = null;

	public HelpHandler() {
		super(PlayerBasics.PERMISSION_HELP);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("help").itMatches();
	}

	@Override
	public boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		if (permissionsToCommandMap == null || nameToCommandMap == null)
			initMaps(sender);

		if (args.length == 0) {
			// Equivalent to /help 1
			printHelp(sender, 1);
			return true;
		} else if (args.length == 1) {
			try {
				int page = Integer.parseInt(args[0]);
				printHelp(sender, page);
			} catch (NumberFormatException nfe) {
				// Requested help on specific command
				printHelp(sender, args[0]);
			}
			return true;
		}

		return false;
	}

	private void printHelp(CommandSender sender, String name) {
		if (nameToCommandMap.containsKey(name)) {
			List<CommandData> commands = nameToCommandMap.get(name);
			for (CommandData data : commands) {
				Messages.send(sender, "&2Command: &e/" + name);
				Messages.send(sender, "  &e- &2" + data.getDescription());
				Messages.send(sender, "  &e- Usage: &2" + data.getUsage());
				Messages.send(sender,
						"  &e- Provided by: &2" + data.getPlugin());
				if (!sender.hasPermission(data.getPermissions())) {
					Messages.send(sender,
							"  &cYou are not authorized to do this");
				}
			}
		} else {
			Messages.send(sender, "&cNo help available for &e/" + name);
		}

	}

	private void printHelp(CommandSender sender, int page) {
		List<CommandData> helpLines = compileHelp(sender);

		int pages = 1 + helpLines.size() / 9;

		if (page < 1 || page > pages) {
			StringBuilder pg = new StringBuilder();

			pg.append("&cInvalid help page: &e/");
			pg.append(page);
			pg.append("&c. Valid pages are: ");

			for (int i = 1; i <= pages; i++) {
				if (i > 1) {
					pg.append("&c, ");
				}
				pg.append("&e");
				pg.append(i);
			}

			Messages.send(sender, pg.toString());
		} else {
			if (page == pages) {
				helpLines = helpLines.subList(9 * (page - 1), helpLines.size());
			} else {
				helpLines = helpLines.subList(9 * (page - 1), 9 * page);
			}

			Messages.send(sender, "&2Help page " + page);
			for (CommandData comm : helpLines) {
				Messages.send(sender, "  &2- &e/" + comm.getName() + "&2: "
						+ comm.getDescription());
			}
		}

	}

	private List<CommandData> compileHelp(CommandSender sender) {
		List<CommandData> data = new ArrayList<CommandData>(30);

		for (Entry<String, List<CommandData>> e : permissionsToCommandMap
				.entrySet()) {
			if (sender.hasPermission(e.getKey()) || e.getKey().isEmpty()) {
				data.addAll(e.getValue());
			}
		}

		return data;
	}

	private void initMaps(CommandSender sender) {
		PluginManager pluginManager = sender.getServer().getPluginManager();

		permissionsToCommandMap = new HashMap<String, List<CommandData>>();
		nameToCommandMap = new HashMap<String, List<CommandData>>();

		for (org.bukkit.plugin.Plugin plugin : pluginManager.getPlugins()) {
			final PluginDescriptionFile desc = plugin.getDescription();
			final Map<String, Map<String, Object>> cmds = getCommands(desc);
			String pluginName = plugin.getDescription().getName().toLowerCase();
			if (cmds != null) {
				for (Entry<String, Map<String, Object>> k : cmds.entrySet()) {

					final String commandname = k.getKey();
					Map<String, Object> data = k.getValue();

					final String permissions = assign(KEY_PERMISSIONS, data);
					final String usage = assign(KEY_USAGE, data);

					final String description = assign(KEY_DESCRIPTION, data);

					if (permissions != null && usage != null
							&& description != null) {
						CommandData c = new CommandData(commandname,
								description, permissions, usage, pluginName);
						MapOp.put(permissionsToCommandMap, permissions, c);
						MapOp.put(nameToCommandMap, commandname, c);
					}

				}
			}

		}

	}

	private String assign(String key, Map<String, Object> data) {
		if (data.containsKey(key))
			return (String) data.get(key);
		return "";
	}

	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Object>> getCommands(
			final PluginDescriptionFile desc) {
		Object o = desc.getCommands();

		return (Map<String, Map<String, Object>>) o;
	}
}
