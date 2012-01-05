package com.jeroensteenbeeke.bk.econchantment;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

public final class BaseData {
	static final Map<Enchantment, Integer> base = new HashMap<Enchantment, Integer>();

	// Convenience method for unit testing
	static final Map<Enchantment, Integer> maxLevel = new HashMap<Enchantment, Integer>();

	static {
		base.put(Enchantment.DAMAGE_ALL, 10); // Sharpness
		maxLevel.put(Enchantment.DAMAGE_ALL, 5); // Sharpness

		base.put(Enchantment.DAMAGE_UNDEAD, 9); // Smite
		maxLevel.put(Enchantment.DAMAGE_UNDEAD, 5); // Smite

		base.put(Enchantment.DAMAGE_ARTHROPODS, 9); // Bane of Anthropods
		maxLevel.put(Enchantment.DAMAGE_ARTHROPODS, 5); // Bane of Anthropods

		base.put(Enchantment.DIG_SPEED, 25); // Efficiency
		maxLevel.put(Enchantment.DIG_SPEED, 5); // Efficiency

		base.put(Enchantment.DURABILITY, 30); // Unbreaking
		maxLevel.put(Enchantment.DURABILITY, 3); // Unbreaking

		base.put(Enchantment.FIRE_ASPECT, 40);
		maxLevel.put(Enchantment.FIRE_ASPECT, 2);

		base.put(Enchantment.KNOCKBACK, 12);
		maxLevel.put(Enchantment.KNOCKBACK, 2);

		base.put(Enchantment.LOOT_BONUS_BLOCKS, 35); // Fortune
		maxLevel.put(Enchantment.LOOT_BONUS_BLOCKS, 3); // Fortune

		base.put(Enchantment.LOOT_BONUS_MOBS, 35); // Looting
		maxLevel.put(Enchantment.LOOT_BONUS_MOBS, 3); // Looting

		base.put(Enchantment.OXYGEN, 30); // Respiration
		maxLevel.put(Enchantment.OXYGEN, 3); // Respiration

		base.put(Enchantment.PROTECTION_ENVIRONMENTAL, 10); // Protection
		maxLevel.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4); // Protection

		base.put(Enchantment.PROTECTION_EXPLOSIONS, 9); // Blast Protection
		maxLevel.put(Enchantment.PROTECTION_EXPLOSIONS, 4); // Blast Protection

		base.put(Enchantment.PROTECTION_FALL, 10); // Feather Fall
		maxLevel.put(Enchantment.PROTECTION_FALL, 4); // Feather Fall

		base.put(Enchantment.PROTECTION_FIRE, 16); // Fire Protection
		maxLevel.put(Enchantment.PROTECTION_FIRE, 4); // Fire Protection

		base.put(Enchantment.PROTECTION_PROJECTILE, 10); // Projectile
															// Protection
		maxLevel.put(Enchantment.PROTECTION_PROJECTILE, 4); // Projectile
															// Protection

		base.put(Enchantment.SILK_TOUCH, 50);
		maxLevel.put(Enchantment.SILK_TOUCH, 1);

		base.put(Enchantment.WATER_WORKER, 20); // Aqua affinity
		maxLevel.put(Enchantment.WATER_WORKER, 1); // Aqua affinity
	}
}
