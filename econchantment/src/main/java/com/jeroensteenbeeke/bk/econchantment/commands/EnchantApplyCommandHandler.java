package com.jeroensteenbeeke.bk.econchantment.commands;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.econchantment.Econchantment;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;

public class EnchantApplyCommandHandler extends PlayerAwareCommandHandler {
	private Jayconomy jayconomy;

	private Econchantment econchantment;

	public EnchantApplyCommandHandler(Econchantment econchantment,
			Jayconomy jayconomy) {
		super(econchantment.getServer(), Econchantment.PERMISSION_USE);

		this.econchantment = econchantment;
		this.jayconomy = jayconomy;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("enchant").andArgLike(0, "apply").itMatches();
	}

	@SuppressWarnings("deprecation")
	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountAtLeast(2).andArgCountAtMost(3)
				.andArgumentLike(1, DECIMAL)
				.andArgumentLikeIfExists(2, DECIMAL).itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		ItemStack itemInHand = player.getItemInHand();

		Integer id = Integer.parseInt(args[1]);
		Integer level = args.length == 3 ? Integer.parseInt(args[2]) : 1;

		Enchantment enchantment = Enchantment.getById(id);

		if (enchantment != null) {
			if (level > 0 && level <= enchantment.getMaxLevel()) {
				if (enchantment.canEnchantItem(itemInHand)) {

					BigDecimal price = econchantment.determineEnchantmentPrice(
							enchantment, level);

					if (price != null) {

						BigDecimal balance = jayconomy.getBalance(player
								.getName());

						if (balance.compareTo(price) >= 0) {
							jayconomy.decreaseBalance(player.getName(), price);
							itemInHand.addEnchantment(enchantment, level);
							Messages.send(player, String.format(
									"&aEnchantment &e%s&a applied",
									enchantment.getName()));

						} else {
							Messages.send(
									player,
									String.format(
											"&cEnchantment &e%s&c costs &e%d&c, you only have &e%d",
											enchantment.getName(),
											price.intValue(),
											balance.intValue()));
						}
					} else {
						Messages.send(
								player,
								String.format(
										"&cEnchantment &e%s&c not available. This is either because it is newer than this plugin, or because of a bug",
										enchantment.getName()));
					}

				} else {
					Messages.send(
							player,
							String.format(
									"&cEnchantment &e%s&c cannot be applied to this item",
									enchantment.getName()));
				}
			} else {
				Messages.send(player, String.format(
						"&cEnchantment &e%s&c has no level &e%d",
						enchantment.getName(), level));
			}
		} else {
			Messages.send(player,
					String.format("&cUnknown enchantment id: &e%d", id));
		}

	}

}
