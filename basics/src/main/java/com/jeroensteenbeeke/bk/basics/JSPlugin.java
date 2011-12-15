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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;

public abstract class JSPlugin extends JavaPlugin {
	private Map<CommandMatcher, CommandHandler> handlers = new HashMap<CommandMatcher, CommandHandler>();

	protected static final Logger log = Logger.getLogger("Minecraft");

	protected final void addCommandHandler(CommandHandler handler) {
		handlers.put(handler.getMatcher(), handler);
	}

	protected final void addListener(Type type, Listener listener,
			Priority priority) {
		getServer().getPluginManager().registerEvent(type, listener, priority,
				this);
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for (Entry<CommandMatcher, CommandHandler> e : handlers.entrySet()) {
			if (e.getKey().matches(command, args)) {
				if (e.getValue().onCommand(sender, command, label, args))
					return true;
			}
		}

		return false;
	}

	protected final void updateType(String table, String field,
			String currentType, String newType) {
		SqlQuery q = getDatabase().createSqlQuery(
				"SHOW COLUMNS FROM " + table + " WHERE Field=:name");
		q.setParameter("name", field);

		SqlRow row = q.findUnique();
		if (row != null) {

			if (currentType.equals(row.getString("Type"))) {

				SqlUpdate update = getDatabase().createSqlUpdate(
						"ALTER TABLE " + table + " MODIFY " + field + " "
								+ newType);

				update.execute();

				log.info("Updated field " + field + " on table " + table
						+ " to type " + newType);
			}
		}
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
