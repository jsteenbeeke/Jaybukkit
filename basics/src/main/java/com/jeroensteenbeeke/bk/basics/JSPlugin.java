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
package com.jeroensteenbeeke.bk.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlUpdate;
import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;

public abstract class JSPlugin extends JavaPlugin {
	private List<CommandHandler> handlers = new ArrayList<CommandHandler>();

	protected static final Logger log = Logger.getLogger("Minecraft");

	protected final void addCommandHandler(CommandHandler handler) {
		handlers.add(handler);
	}

	protected final void addListener(Type type, Listener listener,
			Priority priority) {
		getServer().getPluginManager().registerEvent(type, listener, priority,
				this);
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for (CommandHandler handler : handlers) {
			if (handler.matches(command, args)) {
				if (handler.onCommand(sender, command, label, args))
					return true;
			}
		}

		return false;
	}

	protected final void checkIndex(boolean unique, String table, String name,
			String... columns) {
		if (columns.length == 0)
			return;

		StringBuilder columnExpression = new StringBuilder();
		columnExpression.append("(");
		for (String column : columns) {
			if (columnExpression.length() > 1) {
				columnExpression.append(", ");
			}
			columnExpression.append(column);

		}

		columnExpression.append(")");

		SqlQuery q = getDatabase().createSqlQuery(
				"SHOW INDEX FROM " + table + " WHERE Key_name=:name");
		q.setParameter("name", name);

		if (q.findList().isEmpty()) {
			StringBuilder query = new StringBuilder();
			query.append("CREATE ");
			if (unique)
				query.append("UNIQUE ");
			query.append("INDEX ");
			query.append(name);
			query.append(" ON ");
			query.append(table);
			query.append(" ");
			query.append(columnExpression);

			SqlUpdate update = getDatabase().createSqlUpdate(query.toString());

			update.execute();

			log.info("Created index " + name + " on table " + table);

		}
	}
}
