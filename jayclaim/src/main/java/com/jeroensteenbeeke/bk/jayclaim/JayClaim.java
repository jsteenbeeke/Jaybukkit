package com.jeroensteenbeeke.bk.jayclaim;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayclaim.entities.Claim;
import com.jeroensteenbeeke.bk.jayclaim.listeners.BuildListener;
import com.jeroensteenbeeke.bk.jayclaim.listeners.MovementListener;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.JayconomyAwarePlugin;

public class JayClaim extends JayconomyAwarePlugin {
	public static final String PERMISSION_USE = "jayclaim.use";

	public static final String PERMISSION_ADMIN = "jayclaim.admin";

	public static final String COMMAND_BASE = "jc";

	private Logger logger = Logger.getLogger("Minecraft");

	private Jayconomy jayconomy;

	private ClaimTracker tracker;

	private int freeclaims;

	private File backupFolder;

	@Override
	public void onLoad() {
		super.onLoad();

		getConfig().addDefault("claimsize", 32);
		getConfig().addDefault("freeclaims", 1);
		getConfig().addDefault("baseprice", 200);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onJayconomyInitialized(Jayconomy jayconomy) {
		logger.info("Initializing JayClaim");

		this.jayconomy = jayconomy;
		backupFolder = new File(getDataFolder(), "claimBackups");
		if (!backupFolder.exists()) {
			backupFolder.mkdirs();
		}

		this.tracker = new ClaimTracker(getDatabase(), getConfig().getInt(
				"claimsize"), backupFolder);
		this.freeclaims = getConfig().getInt("freeclaims");

		if (freeclaims <= 0) {
			getServer().getPluginManager().disablePlugin(this);
			logger.severe("YOU MUST HAVE AT LEAST 1 FREE CLAIM PER PLAYER");
			return;
		}

		addListener(new MovementListener(this));
		addListener(new BuildListener(this));

	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = Lists.newArrayListWithCapacity(1);
		classes.add(Claim.class);
		return classes;
	}

	public boolean doClaim(int cost, Player player) {
		if (jayconomy.hasBalance(player, cost)) {
			jayconomy.decreaseBalance(player.getName(), cost);
			return true;
		}

		return false;
	}

	public int getFreeclaims() {
		return freeclaims;
	}

	public int calculateClaimCost(Player player) {
		int base = getConfig().getInt("baseprice");

		int claims = getDatabase().find(Claim.class).where()
				.eq("owner", player.getName()).findRowCount();

		if (claims < freeclaims)
			return 0;

		claims = claims - freeclaims;

		int cost = base;

		for (int i = 0; i < claims; i++) {
			cost = cost * 2;
		}

		return cost;
	}

	public boolean setClaimed(Location location) {
		Claim curr = tracker.getClaimAt(location);

		if (curr == null) {

			Claim claim = new Claim();
			claim.setOwner(null);
			claim.setWorld(location.getWorld().getName());
			claim.setX(location.getBlockX());
			claim.setZ(location.getBlockZ());

			getDatabase().save(claim);

			tracker.storeClaimBackup(claim, location.getWorld());

			tracker.mapClaims();

			return true;
		}

		return false;
	}

	public boolean claim(Location location, Player player) {
		Claim curr = tracker.getClaimAt(location);

		if (curr == null) {
			int cost = calculateClaimCost(player);

			if (doClaim(cost, player)) {

				Claim claim = new Claim();
				claim.setOwner(player.getName());
				claim.setWorld(location.getWorld().getName());
				claim.setX(location.getBlockX());
				claim.setZ(location.getBlockZ());

				getDatabase().save(claim);

				tracker.storeClaimBackup(claim, location.getWorld());

				tracker.mapClaims();
				Messages.send(player, "&eClaim successful");

				return true;
			}

			Messages.send(player, String.format(
					"&cYou require &e%s&c to claim this location",
					jayconomy.formatCurrency(cost)));

			return false;
		}

		Messages.send(player, "&cThis location is already claimed");

		return false;
	}

	public ClaimTracker getTracker() {
		return tracker;
	}

	@Override
	public void onDisable() {

	}
}
