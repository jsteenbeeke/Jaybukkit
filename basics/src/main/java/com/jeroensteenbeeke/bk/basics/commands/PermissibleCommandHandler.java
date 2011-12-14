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

}
