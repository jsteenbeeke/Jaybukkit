package com.jeroensteenbeeke.bk.econchantment.commands;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.econchantment.BaseData;
import com.jeroensteenbeeke.bk.econchantment.Econchantment;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;

public class EnchantInfoCommandHandler extends PlayerAwareCommandHandler {
	private Jayconomy jayconomy;

	private Econchantment econchantment;

	public EnchantInfoCommandHandler(Econchantment econchantment,
			Jayconomy jayconomy) {
		super(econchantment.getServer(), Econchantment.PERMISSION_USE);

		this.econchantment = econchantment;
		this.jayconomy = jayconomy;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("enchant").andArgLike(0, "info").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).andArgumentLike(1, DECIMAL).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {

		Integer id = Integer.parseInt(args[1]);
		Enchantment enchantment = Enchantment.getById(id);

		if (enchantment != null) {
			BigDecimal basePrice = econchantment.determineEnchantmentPrice(
					enchantment, 1);

			if (basePrice != null) {
				String name = BaseData.friendlyNames.get(enchantment);

				int maxLevel = enchantment.getMaxLevel();

				Messages.send(
						player,
						String.format("Enchantment %s (%d)", name,
								enchantment.getId()));

				if (maxLevel == 1) {
					Messages.send(player,
							String.format("  - &e%s&f @ &e%s", name, jayconomy
									.formatCurrency(econchantment
											.determineEnchantmentPrice(
													enchantment, 1))));
				} else {

					for (int i = 1; i <= maxLevel; i++) {
						Messages.send(player, String.format(
								"  - &e%s %s&f @ &e%s", name, latinize(i),
								jayconomy.formatCurrency(econchantment
										.determineEnchantmentPrice(enchantment,
												i))));

					}
				}
			} else {
				Messages.send(
						player,
						String.format(
								"&cEnchantment &e%s&c not available. This is either because it is newer than this plugin, or because of a bug",
								enchantment.getName()));
			}
		} else {
			Messages.send(player,
					String.format("&cUnknown enchantment id: &e%d", id));
		}
	}

	private static final String[] RCODE = { "M", "CM", "D", "CD", "C", "XC",
			"L", "XL", "X", "IX", "V", "IV", "I" };
	private static final int[] BVAL = { 1000, 900, 500, 400, 100, 90, 50, 40,
			10, 9, 5, 4, 1 };

	public static String latinize(int decimal) {
		if (decimal <= 0 || decimal >= 4000) {
			throw new NumberFormatException(
					"Value outside roman numeral range.");
		}
		StringBuilder roman = new StringBuilder();

		for (int i = 0; i < RCODE.length; i++) {
			while (decimal >= BVAL[i]) {
				decimal -= BVAL[i];
				roman.append(RCODE[i]);
			}
		}
		return roman.toString();
	}

}
