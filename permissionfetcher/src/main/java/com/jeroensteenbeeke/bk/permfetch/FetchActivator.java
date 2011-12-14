package com.jeroensteenbeeke.bk.permfetch;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class FetchActivator extends ServerListener {
	private final PermissionsFetcher fetcher;

	public FetchActivator(PermissionsFetcher fetcher) {
		this.fetcher = fetcher;
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getClass() == PermissionsPlugin.class) {
			fetcher.permissionsEnabled((PermissionsPlugin) event.getPlugin());
		}
	}
}
