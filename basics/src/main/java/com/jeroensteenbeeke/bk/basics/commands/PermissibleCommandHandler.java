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
package com.jeroensteenbeeke.bk.basics.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.util.Messages;

public abstract class PermissibleCommandHandler implements CommandHandler {
	protected static enum PermissionPolicy {
		ANY {
			@Override
			public boolean isAuthorized(CommandSender sender,
					String... permissions) {
				for (String permission : permissions) {
					if (sender.hasPermission(permission)) {
						return true;
					}
				}

				return false;
			}
		},
		ALL {
			@Override
			public boolean isAuthorized(CommandSender sender,
					String... permissions) {
				for (String permission : permissions) {
					if (!sender.hasPermission(permission)) {
						return false;
					}
				}

				return true;
			}

		};

		public abstract boolean isAuthorized(CommandSender sender,
				String... permissions);
	}

	private final String[] requiredPermissions;
	private PermissionPolicy policy;

	protected PermissibleCommandHandler(String... requiredPermissions) {
		this(PermissionPolicy.ANY, requiredPermissions);
	}

	public PermissibleCommandHandler(PermissionPolicy policy,
			String... requiredPermissions) {
		this.policy = policy;
		this.requiredPermissions = requiredPermissions;
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (policy.isAuthorized(sender, requiredPermissions)) {
			return onAuthorized(sender, command, label, args);
		} else {
			Messages.send(sender,
					"&cYou are not authorized to perform this action");
			return true;
		}
	}

	public abstract boolean onAuthorized(CommandSender sender, Command command,
			String label, String[] args);

	public MatcherBuilder ifArgIs(int index, String value) {
		return new DefaultMatcherBuilder().andArgIs(index, value);
	}

	public MatcherBuilder ifArgLike(int index, String regex) {
		return new DefaultMatcherBuilder().andArgLike(index, regex);
	}

	public MatcherBuilder ifNameIs(String name) {
		return new DefaultMatcherBuilder().andNameIs(name);
	}

	private static class DefaultMatcherBuilder implements MatcherBuilder {

		private final List<CommandMatcher> matchers;

		public DefaultMatcherBuilder() {
			super();
			this.matchers = new LinkedList<CommandMatcher>();
		}

		@Override
		public MatcherBuilder andNameIs(String name) {
			matchers.add(new NameEquals(name));
			return this;
		}

		@Override
		public MatcherBuilder andArgIs(int index, String value) {
			matchers.add(new ArgEquals(value, index));
			return this;
		}

		@Override
		public MatcherBuilder andArgLike(int index, String regex) {
			matchers.add(new ArgMatches(regex, index));
			return this;
		}

		@Override
		public CommandMatcher itMatches() {
			return new CompoundCommandMatcher(matchers);
		}

	}

	private static class CompoundCommandMatcher implements CommandMatcher {
		private final List<CommandMatcher> matchers;

		public CompoundCommandMatcher(List<CommandMatcher> matchers) {
			super();
			this.matchers = matchers;
		}

		@Override
		public boolean matches(Command command, String[] args) {
			for (CommandMatcher m : matchers) {
				if (!m.matches(command, args)) {
					return false;
				}
			}

			return true;
		}
	}

	private static class NameEquals implements CommandMatcher {
		private final String name;

		public NameEquals(String name) {
			super();
			this.name = name;
		}

		@Override
		public boolean matches(Command command, String[] args) {
			return name.equals(command.getName());
		}
	}

	private static class ArgEquals implements CommandMatcher {
		private final String arg;

		private final int index;

		public ArgEquals(String arg, int index) {
			super();
			this.arg = arg;
			this.index = index;
		}

		@Override
		public boolean matches(Command command, String[] args) {
			return args.length > index && args[index].equals(arg);
		}
	}

	private static class ArgMatches implements CommandMatcher {
		private final String regex;

		private final int index;

		public ArgMatches(String regex, int index) {
			super();
			this.regex = regex;
			this.index = index;
		}

		@Override
		public boolean matches(Command command, String[] args) {
			return args.length > index && args[index].matches(regex);
		}
	}

}
