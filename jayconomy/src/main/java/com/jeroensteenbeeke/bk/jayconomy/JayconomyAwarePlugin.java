package com.jeroensteenbeeke.bk.jayconomy;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public abstract class JayconomyAwarePlugin extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	private Jayconomy jayconomy;

	@Override
	public final void onEnable() {
		Plugin jc = getServer().getPluginManager().getPlugin("jayconomy");

		if (jc != null) {
			if (jc.isEnabled()) {
				logger.info("Jayconomy already enabled");

				onJayconomyInitialized((Jayconomy) jc);
			} else {
				logger.info("Jayconomy not yet enabled");

				addListener(new InitJayconomyListener());
			}
		} else {
			logger.severe("Could not find Jayconomy!");
		}
	}

	public Jayconomy getJayconomy() {
		return jayconomy;
	}

	public abstract void onJayconomyInitialized(Jayconomy jayconomy);

	public class InitJayconomyListener implements Listener {
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginEnable(PluginEnableEvent event) {
			if (event.getPlugin() instanceof Jayconomy) {
				onJayconomyInitialized((Jayconomy) event.getPlugin());
			}
		}
	}
}
