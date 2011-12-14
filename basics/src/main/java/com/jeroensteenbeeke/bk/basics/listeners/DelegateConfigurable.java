package com.jeroensteenbeeke.bk.basics.listeners;

import com.jeroensteenbeeke.bk.basics.Configurable;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

final class DelegateConfigurable extends Configurable
{
	public DelegateConfigurable(JSPlugin plugin)
	{
		super(plugin);
	}
}