package com.jeroensteenbeeke.bk.ville;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class Ville extends JSPlugin {
	public static final String PERMISSION_USE = "ville.use";

	public static final String PERMISSION_ADMIN = "ville.admin";

	private Logger logger = Logger.getLogger("Minecraft");

	private int minimumDistance;

	private int price;

	@Override
	public void onEnable() {
		logger.info("Enabled ville plugin");

		minimumDistance = getConfig().getInt("minimumDistance", 1000);
		price = getConfig().getInt("price", 5000);
		saveConfig();

		setupDatabase();

		Plugin jc = getServer().getPluginManager().getPlugin("jayconomy");

		if (jc != null) {
			if (jc.isEnabled()) {
				logger.info("Jayconomy already enabled");

				initJayconomy((Jayconomy) jc);
			} else {
				logger.info("Jayconomy not yet enabled");

				addListener(Type.PLUGIN_ENABLE, new ServerListener() {
					@Override
					public void onPluginEnable(PluginEnableEvent event) {
						super.onPluginEnable(event);

						if (event.getPlugin() instanceof Jayconomy) {
							initJayconomy((Jayconomy) event.getPlugin());
						}
					}
				}, Priority.Normal);
			}
		} else {
			logger.severe("Could not find Jayconomy!");
		}

	}

	public int getMinimumDistance() {
		return minimumDistance;
	}

	public int getPrice() {
		return price;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(VillageLocation.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Ville database");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(VillageLocation.class);

		return classes;
	}

	void initJayconomy(Jayconomy jayconomy) {
		logger.info("Linking ville to jayconomy");

		addCommandHandler(new VilleCommandHandler(this, jayconomy));
		addCommandHandler(new VilleAdminCommandHandler(this));
		addCommandHandler(new JurisdictionCommandHandler(this));
	}

	@Override
	public void onDisable() {

	}

}
