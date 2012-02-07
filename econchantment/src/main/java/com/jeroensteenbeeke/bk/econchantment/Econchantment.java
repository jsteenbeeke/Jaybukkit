package com.jeroensteenbeeke.bk.econchantment;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.bukkit.enchantments.Enchantment;

import com.jeroensteenbeeke.bk.econchantment.commands.EnchantApplyCommandHandler;
import com.jeroensteenbeeke.bk.econchantment.commands.EnchantInfoCommandHandler;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.JayconomyAwarePlugin;

public class Econchantment extends JayconomyAwarePlugin {
	public static final String PERMISSION_USE = "econchantment.use";

	private Logger logger = Logger.getLogger("Minecraft");

	private BigDecimal levelPrice;

	@Override
	public void onLoad() {
		getConfig().addDefault("levelPrice", 500);
		getConfig().options().copyDefaults(true);

		saveConfig();

	}

	@Override
	public void onJayconomyInitialized(Jayconomy jayconomy) {
		levelPrice = new BigDecimal(getConfig().getInt("levelPrice", 500));
		logger.info("Initializing Econchantment");

		addCommandHandler(new EnchantApplyCommandHandler(this, jayconomy));
		addCommandHandler(new EnchantInfoCommandHandler(this, jayconomy));
	}

	@Override
	public void onDisable() {

	}

	public BigDecimal determineEnchantmentPrice(Enchantment enchantment,
			int level) {
		if (BaseData.base.containsKey(enchantment)) {

			return levelPrice.multiply(new BigDecimal(level)).multiply(
					new BigDecimal(BaseData.base.get(enchantment)));
		}

		return null;
	}
}
