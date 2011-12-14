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
