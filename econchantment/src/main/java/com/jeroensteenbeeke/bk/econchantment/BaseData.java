package com.jeroensteenbeeke.bk.econchantment;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

public final class BaseData {
	static final Map<Enchantment, Integer> base = new HashMap<Enchantment, Integer>();

	// Convenience method for unit testing
	static final Map<Enchantment, Integer> maxLevel = new HashMap<Enchantment, Integer>();

	public static final Map<Enchantment, String> friendlyNames = new HashMap<Enchantment, String>();

	static {
		base.put(Enchantment.DAMAGE_ALL, 10); // Sharpness
		maxLevel.put(Enchantment.DAMAGE_ALL, 5); // Sharpness
		friendlyNames.put(Enchantment.DAMAGE_ALL, "Sharpness");

		base.put(Enchantment.DAMAGE_UNDEAD, 9); // Smite
		maxLevel.put(Enchantment.DAMAGE_UNDEAD, 5); // Smite
		friendlyNames.put(Enchantment.DAMAGE_UNDEAD, "Smite");

		base.put(Enchantment.DAMAGE_ARTHROPODS, 9); // Bane of Anthropods
		maxLevel.put(Enchantment.DAMAGE_ARTHROPODS, 5); // Bane of Anthropods
		friendlyNames.put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Anthropods");

		base.put(Enchantment.DIG_SPEED, 25); // Efficiency
		maxLevel.put(Enchantment.DIG_SPEED, 5); // Efficiency
		friendlyNames.put(Enchantment.DIG_SPEED, "Efficiency");

		base.put(Enchantment.DURABILITY, 30); // Unbreaking
		maxLevel.put(Enchantment.DURABILITY, 3); // Unbreaking
		friendlyNames.put(Enchantment.DURABILITY, "Unbreaking");

		base.put(Enchantment.FIRE_ASPECT, 40);
		maxLevel.put(Enchantment.FIRE_ASPECT, 2);
		friendlyNames.put(Enchantment.FIRE_ASPECT, "Fire Aspect");

		base.put(Enchantment.KNOCKBACK, 12);
		maxLevel.put(Enchantment.KNOCKBACK, 2);
		friendlyNames.put(Enchantment.KNOCKBACK, "Knockback");

		base.put(Enchantment.LOOT_BONUS_BLOCKS, 35); // Fortune
		maxLevel.put(Enchantment.LOOT_BONUS_BLOCKS, 3); // Fortune
		friendlyNames.put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");

		base.put(Enchantment.LOOT_BONUS_MOBS, 35); // Looting
		maxLevel.put(Enchantment.LOOT_BONUS_MOBS, 3); // Looting
		friendlyNames.put(Enchantment.LOOT_BONUS_MOBS, "Looting");

		base.put(Enchantment.OXYGEN, 30); // Respiration
		maxLevel.put(Enchantment.OXYGEN, 3); // Respiration
		friendlyNames.put(Enchantment.OXYGEN, "Respiration");

		base.put(Enchantment.PROTECTION_ENVIRONMENTAL, 10); // Protection
		maxLevel.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4); // Protection
		friendlyNames.put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");

		base.put(Enchantment.PROTECTION_EXPLOSIONS, 9); // Blast Protection
		maxLevel.put(Enchantment.PROTECTION_EXPLOSIONS, 4); // Blast Protection
		friendlyNames
				.put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");

		base.put(Enchantment.PROTECTION_FALL, 10); // Feather Fall
		maxLevel.put(Enchantment.PROTECTION_FALL, 4); // Feather Fall
		friendlyNames.put(Enchantment.PROTECTION_FALL, "Feather Fall");

		base.put(Enchantment.PROTECTION_FIRE, 16); // Fire Protection
		maxLevel.put(Enchantment.PROTECTION_FIRE, 4); // Fire Protection
		friendlyNames.put(Enchantment.PROTECTION_FIRE, "Fire Protection");

		base.put(Enchantment.PROTECTION_PROJECTILE, 10); // Projectile
															// Protection
		maxLevel.put(Enchantment.PROTECTION_PROJECTILE, 4); // Projectile
															// Protection
		friendlyNames.put(Enchantment.PROTECTION_PROJECTILE,
				"Projectile Protection");

		base.put(Enchantment.SILK_TOUCH, 50);
		maxLevel.put(Enchantment.SILK_TOUCH, 1);
		friendlyNames.put(Enchantment.SILK_TOUCH, "Silk Touch");

		base.put(Enchantment.WATER_WORKER, 20); // Aqua affinity
		maxLevel.put(Enchantment.WATER_WORKER, 1); // Aqua affinity
		friendlyNames.put(Enchantment.WATER_WORKER, "Aqua Affinitiy");

		base.put(Enchantment.ARROW_DAMAGE, 11);
		maxLevel.put(Enchantment.ARROW_DAMAGE, 5);
		friendlyNames.put(Enchantment.ARROW_DAMAGE, "Power");

		base.put(Enchantment.ARROW_KNOCKBACK, 13);
		maxLevel.put(Enchantment.ARROW_KNOCKBACK, 3);
		friendlyNames.put(Enchantment.ARROW_KNOCKBACK, "Punch");

		base.put(Enchantment.ARROW_FIRE, 41);
		maxLevel.put(Enchantment.ARROW_FIRE, 1);
		friendlyNames.put(Enchantment.ARROW_FIRE, "Flame");

		base.put(Enchantment.ARROW_INFINITE, 50);
		maxLevel.put(Enchantment.ARROW_INFINITE, 1);
		friendlyNames.put(Enchantment.ARROW_INFINITE, "Infinity");

		base.put(Enchantment.THORNS, 30);
		maxLevel.put(Enchantment.THORNS, 3);
		friendlyNames.put(Enchantment.THORNS, "Thorns");

	}
}
