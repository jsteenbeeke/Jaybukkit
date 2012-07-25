package com.jeroensteenbeeke.bk.jaycore;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jaycore.entities.DeathBan;
import com.jeroensteenbeeke.bk.jaycore.entities.TorchGiven;
import com.jeroensteenbeeke.bk.jaycore.listeners.PlayerListener;

public class JayCore extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled jaycore plugin");
		logger.info("Rewiring recipes");

		setupDatabase();

		// Set<Material> blacklisted = Sets.newHashSet(Material.GOLDEN_APPLE,
		// Material.SPECKLED_MELON);
		//
		// List<Recipe> allRecipes = Lists.newArrayList();
		//
		// for (Material m : Material.values()) {
		// if (!blacklisted.contains(m)) {
		// ItemStack s = new ItemStack(m);
		// allRecipes.addAll(getServer().getRecipesFor(s));
		// }
		// }
		//
		// getServer().clearRecipes();
		//
		// for (Recipe r : allRecipes) {
		// getServer().addRecipe(r);
		// }
		//
		// getServer().addRecipe(
		// new ShapedRecipe(new ItemStack(Material.GOLDEN_APPLE, 1))
		// .shape("xxx", "xyx", "xxx")
		// .setIngredient('x', Material.GOLD_INGOT)
		// .setIngredient('y', Material.APPLE));
		//
		// getServer().addRecipe(
		// new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON, 1))
		// .addIngredient(Material.MELON).addIngredient(
		// Material.GOLD_BLOCK));Is

		addListener(new PlayerListener(getDatabase(), getConfig().getLong(
				"banDuration", 1000L * 60L * 60L * 24L)));

	}

	private void setupDatabase() {
		try {
			getDatabase().find(DeathBan.class).findRowCount();
			getDatabase().find(TorchGiven.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing JayCore database");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(DeathBan.class);
		classes.add(TorchGiven.class);

		return classes;
	}

	@Override
	public void onDisable() {

	}
}
