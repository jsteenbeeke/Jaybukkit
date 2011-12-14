package com.jeroensteenbeeke.bk.basics.listeners;

import org.bukkit.event.block.BlockListener;
import org.bukkit.util.config.ConfigurationNode;

import com.jeroensteenbeeke.bk.basics.Configurable;
import com.jeroensteenbeeke.bk.basics.IConfigurable;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class ConfigurableBlockListener extends BlockListener implements IConfigurable
{
	private Configurable delegate;

	public ConfigurableBlockListener(JSPlugin plugin)
	{
		delegate = new DelegateConfigurable(plugin);
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
