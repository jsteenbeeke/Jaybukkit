package com.jeroensteenbeeke.bk.econchantment;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.econchantment.commands.EnchantApplyCommandHandler;
import com.jeroensteenbeeke.bk.econchantment.commands.EnchantInfoCommandHandler;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;

public class Econchantment extends JSPlugin {
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
	public void onEnable() {
		logger.info("Enabled econchantment plugin");

		Plugin jc = getServer().getPluginManager().getPlugin("jayconomy");

		levelPrice = new BigDecimal(getConfig().getInt("levelPrice", 500));

		if (jc != null) {
			if (jc.isEnabled()) {
				logger.info("Jayconomy already enabled");

				initJayconomy((Jayconomy) jc);
			} else {
				logger.info("Jayconomy not yet enabled");

				addListener(Type.PLUGIN_ENABLE, new ServerListener() {
					@Override
					public void onPluginEnable(PluginEnableEvent event) {
						super.onPluginEnable(event);

						if (event.getPlugin() instanceof Jayconomy) {
							initJayconomy((Jayconomy) event.getPlugin());
						}
					}
				}, Priority.Normal);
			}
		} else {
			logger.severe("Could not find Jayconomy!");
		}
	}

	void initJayconomy(Jayconomy jayconomy) {
		logger.info("Linking Econchantment to jayconomy");

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
