package com.jeroensteenbeeke.bk.jaycore;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jaycore.entities.DeathBan;
import com.jeroensteenbeeke.bk.jaycore.listeners.PlayerListener;

public class JayCore extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled jaycore plugin");
		logger.info("Rewiring recipes");

		Set<Material> blacklisted = Sets.newHashSet(Material.GOLDEN_APPLE,
				Material.SPECKLED_MELON);

		List<Recipe> allRecipes = Lists.newArrayList();

		for (Material m : Material.values()) {
			if (!blacklisted.contains(m)) {
				ItemStack s = new ItemStack(m);
				allRecipes.addAll(getServer().getRecipesFor(s));
			}
		}

		getServer().clearRecipes();

		for (Recipe r : allRecipes) {
			getServer().addRecipe(r);
		}

		getServer().addRecipe(
				new ShapedRecipe(new ItemStack(Material.GOLDEN_APPLE, 1))
						.shape("xxx", "xyx", "xxx")
						.setIngredient('x', Material.GOLD_INGOT)
						.setIngredient('y', Material.APPLE));

		getServer().addRecipe(
				new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON, 1))
						.addIngredient(Material.MELON).addIngredient(
								Material.GOLD_BLOCK));

		addListener(new PlayerListener(getDatabase(), getConfig().getLong(
				"banDuration", 1000L * 60L * 60L * 24L)));

	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(DeathBan.class);

		return classes;
	}

	@Override
	public void onDisable() {

	}
}
