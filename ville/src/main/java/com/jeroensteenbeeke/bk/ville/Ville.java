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
package com.jeroensteenbeeke.bk.ville;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.ville.commands.JurisdictionCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleAddBuilderCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleAdminMakeFreeBuildCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleAdminSetCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleAdminUnmakeFreeBuildCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleAdminUnsetCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleApproveMeCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleCedeCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleCheckCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleClaimCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleFreeBuildCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleRemoveBuilderCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleRestrictCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleUnclaimCommandHandler;
import com.jeroensteenbeeke.bk.ville.commands.VilleUnrestrictCommandHandler;
import com.jeroensteenbeeke.bk.ville.entities.ApprovedPlayer;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;
import com.jeroensteenbeeke.bk.ville.entities.VilleBuilder;
import com.jeroensteenbeeke.bk.ville.listeners.BuildPermissionListener;
import com.jeroensteenbeeke.bk.ville.listeners.FluidListener;
import com.jeroensteenbeeke.bk.ville.listeners.LoginListener;

public class Ville extends JSPlugin {
	public static final String PERMISSION_USE = "ville.use";

	public static final String PERMISSION_ADMIN = "ville.admin";

	public static final String PERMISSION_PREMIUM = "premiummembers.premium";

	private Logger logger = Logger.getLogger("Minecraft");

	private int minimumDistance;

	private int claimPrice;

	private int restrictPrice;

	private int approvePrice;

	private VilleLocations locations;

	@Override
	public void onLoad() {
		getConfig().addDefault("minimumDistance", 1000);
		getConfig().addDefault("price", 5000);
		getConfig().addDefault("restrictPrice", 12000);
		getConfig().addDefault("approvePrice", 9000);
		getConfig().options().copyDefaults(true);

		saveConfig();

	}

	@Override
	public void onEnable() {
		logger.info("Enabled ville plugin");

		minimumDistance = getConfig().getInt("minimumDistance", 1000);
		claimPrice = getConfig().getInt("price", 5000);
		restrictPrice = getConfig().getInt("restrictPrice", 12000);
		approvePrice = getConfig().getInt("approvePrice", 9000);

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

	public VilleLocations getLocations() {
		return locations;
	}

	public int getMinimumDistance() {
		return minimumDistance;
	}

	public int getClaimPrice() {
		return claimPrice;
	}

	public int getRestrictPrice() {
		return restrictPrice;
	}

	public int getApprovePrice() {
		return approvePrice;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(VillageLocation.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Ville database");
			installDDL();
		}
		addColumnIfNotExists("village_location", "restricted",
				"tinyint(1) not null default 0");
		try {
			getDatabase().find(VilleBuilder.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Adding VilleBuilder table");
			installDDL();
		}
		try {
			getDatabase().find(ApprovedPlayer.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Adding VilleBuilder table");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(VillageLocation.class);
		classes.add(VilleBuilder.class);
		classes.add(ApprovedPlayer.class);

		return classes;
	}

	void initJayconomy(Jayconomy jayconomy) {
		logger.info("Linking ville to jayconomy");

		locations = new VilleLocations(this);

		addCommandHandler(new VilleCheckCommandHandler(this));
		addCommandHandler(new VilleClaimCommandHandler(this, jayconomy));
		addCommandHandler(new VilleUnclaimCommandHandler(this));
		addCommandHandler(new VilleAdminSetCommandHandler(this));
		addCommandHandler(new VilleAdminUnsetCommandHandler(this));
		addCommandHandler(new JurisdictionCommandHandler(this));
		addCommandHandler(new VilleRestrictCommandHandler(this, jayconomy));
		addCommandHandler(new VilleUnrestrictCommandHandler(this));
		addCommandHandler(new VilleAddBuilderCommandHandler(this));
		addCommandHandler(new VilleRemoveBuilderCommandHandler(this));
		addCommandHandler(new VilleCedeCommandHandler(this));
		addCommandHandler(new VilleAdminMakeFreeBuildCommandHandler(this));
		addCommandHandler(new VilleAdminUnmakeFreeBuildCommandHandler(this));
		addCommandHandler(new VilleApproveMeCommandHandler(this, jayconomy));
		addCommandHandler(new VilleFreeBuildCommandHandler(this));

		BlockListener listener = new BuildPermissionListener(this);

		addListener(Type.BLOCK_PLACE, listener, Priority.Highest);

		addListener(Type.BLOCK_BREAK, listener, Priority.Highest);

		addListener(Type.BLOCK_DAMAGE, listener, Priority.Highest);

		addListener(Type.PLAYER_BUCKET_EMPTY, new FluidListener(this),
				Priority.Highest);
		addListener(Type.PLAYER_JOIN, new LoginListener(this), Priority.Monitor);
	}

	public void approvePlayer(Player player) {
		locations.getApprovedPlayers().add(player.getName());
	}

	@Override
	public void onDisable() {

	}

}
