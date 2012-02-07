package com.jeroensteenbeeke.bk.jayclaim;

import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;

public class JayClaim extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled jayclaim plugin");

		setupDatabase();

		Plugin jc = getServer().getPluginManager().getPlugin("jayconomy");

		if (jc != null) {
			if (jc.isEnabled()) {
				logger.info("Jayconomy already enabled");

				initJayconomy((Jayconomy) jc);
			} else {
				logger.info("Jayconomy not yet enabled");

				addListener(new InitJayconomyListener());
			}
		} else {
			logger.severe("Could not find Jayconomy!");
		}
	}

	private void setupDatabase() {
		try {
			// getDatabase().find(INSERT_CLASS_HERE.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing JayClaim database");
			installDDL();
		}
	}

	void initJayconomy(Jayconomy jayconomy) {
		logger.info("Linking JayClaim to Jayconomy");
	}

	@Override
	public void onDisable() {

	}

	public class InitJayconomyListener implements Listener {
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginEnable(PluginEnableEvent event) {
			if (event.getPlugin() instanceof Jayconomy) {
				initJayconomy((Jayconomy) event.getPlugin());
			}
		}
	}
}
