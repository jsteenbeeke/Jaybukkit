package com.jeroensteenbeeke.bk.basics.listeners;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.util.config.ConfigurationNode;

import com.jeroensteenbeeke.bk.basics.Configurable;
import com.jeroensteenbeeke.bk.basics.IConfigurable;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class ConfigurablePlayerListener extends PlayerListener implements IConfigurable
{
	private final Configurable delegate;

	protected ConfigurablePlayerListener(JSPlugin plugin)
	{
		this.delegate = new DelegateConfigurable(plugin);
	}

	@Override
	public ConfigurationNode getConfigFile(String name)
	{
		return delegate.getConfigFile(name);
	}

	@Override
	public void saveConfigFile(String name, ConfigurationNode node)
	{
		delegate.saveConfigFile(name, node);
	}

}
