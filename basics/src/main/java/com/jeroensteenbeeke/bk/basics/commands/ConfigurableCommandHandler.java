package com.jeroensteenbeeke.bk.basics.commands;

import com.jeroensteenbeeke.bk.basics.Configurable;
import com.jeroensteenbeeke.bk.basics.JSPlugin;

public abstract class ConfigurableCommandHandler extends Configurable implements CommandHandler
{
	protected ConfigurableCommandHandler(JSPlugin plugin)
	{
		super(plugin);
	}

}
