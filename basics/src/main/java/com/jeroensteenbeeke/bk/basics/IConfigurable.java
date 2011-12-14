package com.jeroensteenbeeke.bk.basics;

import org.bukkit.util.config.ConfigurationNode;

public interface IConfigurable
{
	ConfigurationNode getConfigFile(String name);

	void saveConfigFile(String name, ConfigurationNode node);
}
