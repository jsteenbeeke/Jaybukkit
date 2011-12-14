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
