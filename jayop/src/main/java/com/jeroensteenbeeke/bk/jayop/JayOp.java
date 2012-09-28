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
package com.jeroensteenbeeke.bk.jayop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jayop.commands.BanCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.ClearInventoryCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.CombustCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.FreezeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.GiveItemCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.KickCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.ListFrozenCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.SetSpawnLocationCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.SuspendCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportHomeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportOthersCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TeleportToMeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.TimeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.UnbanCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.UnfreezeCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.UnsuspendCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.WeatherCommandHandler;
import com.jeroensteenbeeke.bk.jayop.commands.ZapCommandHandler;
import com.jeroensteenbeeke.bk.jayop.entities.Suspension;
import com.jeroensteenbeeke.bk.jayop.listeners.FrozenPlayerListener;
import com.jeroensteenbeeke.bk.jayop.listeners.ProtectionListener;
import com.jeroensteenbeeke.bk.jayop.listeners.SuspendedPlayerListener;

public class JayOp extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final String PERMISSION_ENVIRONMENT = "jayop.environment";

	public static final String PERMISSION_LOCATIONAL = "jayop.location";

	public static final String PERMISSION_ENFORCEMENT = "jayop.enforcement";

	public static final String PERMISSION_INVENTORY = "jayop.inventory";

	public static final String PERMISSION_MODIFY_WORLD = "jayop.world.modify";

	private Set<String> frozen = new HashSet<String>();

	@Override
	public void onLoad() {
		getConfig().addDefault("frozen", new LinkedList<String>());
		getConfig().options().copyDefaults(true);

		saveConfig();
	}

	@Override
	public void onEnable() {
		logger.info("Enabled JayOp plugin");

		setupDatabase();

		this.frozen = new HashSet<String>(getConfig().getStringList("frozen"));

		addListener(new SuspendedPlayerListener(this));
		addListener(new FrozenPlayerListener(this));
		addListener(new ProtectionListener(this));

		addCommandHandler(new WeatherCommandHandler(this));
		addCommandHandler(new TeleportCommandHandler(this));
		addCommandHandler(new TeleportToMeCommandHandler(this));
		addCommandHandler(new TeleportOthersCommandHandler(this));
		addCommandHandler(new TimeCommandHandler(this));
		addCommandHandler(new KickCommandHandler());
		addCommandHandler(new SuspendCommandHandler(this));
		addCommandHandler(new BanCommandHandler());
		addCommandHandler(new UnbanCommandHandler());
		addCommandHandler(new UnsuspendCommandHandler(this));
		addCommandHandler(new GiveItemCommandHandler());
		addCommandHandler(new ClearInventoryCommandHandler());
		addCommandHandler(new TeleportHomeCommandHandler(this));
		addCommandHandler(new CombustCommandHandler(this));
		addCommandHandler(new ZapCommandHandler(this));
		addCommandHandler(new FreezeCommandHandler(this));
		addCommandHandler(new UnfreezeCommandHandler(this));
		addCommandHandler(new ListFrozenCommandHandler(this));
		addCommandHandler(new SetSpawnLocationCommandHandler(this));

	}

	public void freeze(Player player) {
		frozen.add(player.getName());
	}

	public void unfreeze(Player player) {
		frozen.remove(player.getName());
	}

	public Set<String> getFrozen() {
		return frozen;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(Suspension.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Waypoint database");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(Suspension.class);

		return classes;
	}

	@Override
	public void onDisable() {
		getConfig().set("frozen", new ArrayList<String>(frozen));
		saveConfig();
	}
}
