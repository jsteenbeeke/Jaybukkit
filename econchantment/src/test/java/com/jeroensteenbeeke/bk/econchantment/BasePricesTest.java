package com.jeroensteenbeeke.bk.econchantment;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.Modifier;

import org.bukkit.enchantments.Enchantment;
import org.junit.Before;
import org.junit.Test;

public class BasePricesTest {
	private List<Enchantment> declaredEnchantments;

	private Map<Enchantment, String> names = new HashMap<Enchantment, String>();

	@Before
	public void initEnchantments() throws IllegalArgumentException,
			IllegalAccessException {
		declaredEnchantments = new ArrayList<Enchantment>(20);

		for (Field f : Enchantment.class.getFields()) {
			if (Modifier.isFinal(f.getModifiers())
					&& Modifier.isStatic(f.getModifiers())
					&& Enchantment.class.isAssignableFrom(f.getType())) {
				Enchantment e = (Enchantment) f.get(null);
				if (e != null) {
					declaredEnchantments.add(e);
					names.put(e, f.getName());
				}
			}
		}
	}

	@Test
	public void checkBaseValues() {
		assertTrue("There is at least 1 enchantment",
				declaredEnchantments.size() > 0);

		for (Enchantment e : declaredEnchantments) {
			String name = names.get(e);

			assertTrue(String.format("Enchantment %s has a base price", name),
					BaseData.base.containsKey(e));

			// Forum formatting
			// System.out.printf("[b]%s[/b]", name);
			// System.out.println();
			// System.out.println("[list=1]");
			// for (int i = 1; i <= BaseData.maxLevel.get(e); i++) {
			// int price = 250 * i * BaseData.base.get(e);
			//
			// System.out.printf("[*] %d crowns", price);
			// System.out.println();
			// }
			// System.out.println("[/list]");
		}
	}
}
