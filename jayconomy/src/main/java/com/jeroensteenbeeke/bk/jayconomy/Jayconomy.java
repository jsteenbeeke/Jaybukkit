/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.jayconomy;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.jayconomy.commands.BalanceHandler;
import com.jeroensteenbeeke.bk.jayconomy.commands.ReclaimHandler;
import com.jeroensteenbeeke.bk.jayconomy.commands.SetMaxHandler;
import com.jeroensteenbeeke.bk.jayconomy.commands.SetPriceHandler;
import com.jeroensteenbeeke.bk.jayconomy.commands.SignOwnerHandler;
import com.jeroensteenbeeke.bk.jayconomy.commands.TransferHandler;
import com.jeroensteenbeeke.bk.jayconomy.entities.Balance;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyDeal;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyMaterial;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyStash;
import com.jeroensteenbeeke.bk.jayconomy.listeners.SignBlockListener;
import com.jeroensteenbeeke.bk.jayconomy.listeners.SignExplodeListener;
import com.jeroensteenbeeke.bk.jayconomy.listeners.SignPlayerListener;

public class Jayconomy extends JSPlugin {
	public static final String PERMISSION_BALANCE = "jayconomy.balance";

	public static final String PERMISSION_PLACE = "jayconomy.sign.place";

	public static final String PERMISSION_USE = "jayconomy.sign.use";

	public static final String PERMISSION_VIEW_OWNER = "jayconomy.sign.viewowner";

	public static final String PERMISSION_TRANSFER = "jayconomy.transfer";

	public static final String PERMISSION_BUILD_BASIC = "permissions.build";

	public static final HashSet<Byte> transparent = new HashSet<Byte>();

	static {
		transparent.add(new Integer(Material.AIR.getId()).byteValue());
		transparent.add(new Integer(Material.TORCH.getId()).byteValue());
	}

	private Logger logger = Logger.getLogger("Minecraft");

	private String currency;

	private boolean prefix;

	private boolean spaceBetween;

	@Override
	public void onEnable() {
		logger.info("Enabled jayconomy plugin");

		setupDatabase();
		setupConfiguration();

		this.currency = getConfig().getString("currency", "$");
		this.prefix = getConfig().getBoolean("currencyPrefixed", true);
		this.spaceBetween = getConfig().getBoolean(
				"spaceBetweenCurrencyAndAmount", false);
		saveConfig();

		addCommandHandler(new BalanceHandler(this));
		addCommandHandler(new TransferHandler(this));
		addCommandHandler(new SignOwnerHandler(this));
		addCommandHandler(new ReclaimHandler(this));
		addCommandHandler(new SetPriceHandler(this));
		addCommandHandler(new SetMaxHandler(this));

		addListener(new SignExplodeListener(this));
		addListener(new SignPlayerListener(this));
		addListener(new SignBlockListener(this));
	}

	private void setupConfiguration() {
		Worth.initializeWorth(getDatabase());

	}

	private void setupDatabase() {
		try {
			getDatabase().find(Balance.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing Balance database");
			installDDL();
		}
		try {
			for (JayconomySign sign : getDatabase().find(JayconomySign.class)
					.findList()) {
				sign.getSubtype();
				break;
			}
		} catch (PersistenceException ex) {
			logger.info("Updating JayconomySign");
			getDatabase().createSqlUpdate(
					"ALTER TABLE jayconomy_sign ADD subtype TINYINT NULL")
					.execute();
		}
		try {
			for (JayconomyDeal deal : getDatabase().find(JayconomyDeal.class)
					.findList()) {
				deal.getSubType();
				break;
			}
		} catch (PersistenceException ex) {
			logger.info("Updating JayconomyDeal");
			getDatabase().createSqlUpdate(
					"ALTER TABLE jayconomy_deal ADD sub_type TINYINT NULL")
					.execute();
		}
		checkIndex(true, "jayconomy_sign", "location", "x", "y", "z", "world");
		updateType("jayconomy_sign", "subtype", "tinyint(4)", "integer");
		updateType("jayconomy_deal", "sub_type", "tinyint(4)", "integer");
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(Balance.class);
		classes.add(JayconomySign.class);
		classes.add(JayconomyStash.class);
		classes.add(JayconomyMaterial.class);
		classes.add(JayconomyDeal.class);

		return classes;
	}

	@Override
	public void onDisable() {

	}

	public BigDecimal getBalance(String playerName) {
		Balance balance = getDatabase().createQuery(Balance.class).where()
				.eq("playerName", playerName).findUnique();

		if (balance != null) {
			return balance.getBalance();
		}

		return BigDecimal.ZERO;
	}

	public void decreaseBalance(String player, BigDecimal worth) {
		Balance balance = getDatabase().createQuery(Balance.class).where()
				.eq("playerName", player).findUnique();

		balance.setBalance(balance.getBalance().subtract(worth));
		getDatabase().update(balance);

	}

	public void increaseBalance(String player, BigDecimal worth) {
		Balance balance = getDatabase().createQuery(Balance.class).where()
				.eq("playerName", player).findUnique();

		if (balance == null) {
			balance = new Balance();
			balance.setBalance(worth);
			balance.setPlayerName(player);
			getDatabase().save(balance);
		} else {
			balance.setBalance(balance.getBalance().add(worth));
			getDatabase().update(balance);
		}
	}

	public String formatCurrency(BigDecimal amount) {
		StringBuilder builder = new StringBuilder();

		if (prefix) {
			builder.append(currency);
			if (spaceBetween) {
				builder.append(' ');
			}
		}
		builder.append(amount);
		if (!prefix) {
			if (spaceBetween) {
				builder.append(' ');
			}
			builder.append(currency);
		}

		return builder.toString();
	}

	public void updateSellSign(Sign sign, JayconomySign jsign) {
		sign.setLine(3, "\u00A7e0 \u00A70left");

		sign.setLine(0, "\u00A7a[ Sell ]\u00A70");
		sign.setLine(1, String.format("\u00A7e%s %s", jsign.getAmount(),
				formatMaterial(jsign)));

		sign.setLine(2,
				String.format("\u00A7e%s", formatCurrency(jsign.getValue())));

		if (jsign.getMax() != null) {
			if (jsign.getMax() > 0) {
				sign.setLine(3, String.format("\u00A7e%s left", jsign.getMax()));
			} else {
				sign.setLine(3, "\u00A7cOut of stock");
			}
		} else {
			sign.setLine(3, "");
		}

		sign.update();
	}

	public void updateBuySign(Sign sign, JayconomySign jsign) {
		sign.setLine(0, "\u00A7a[ Buy ]\u00A70");
		sign.setLine(1, String.format("\u00A7e%s \u00A70 %s",
				jsign.getAmount(), formatMaterial(jsign)));

		sign.setLine(
				2,
				String.format("\u00A7e%s\u00A70",
						formatCurrency(jsign.getValue())));

		if (jsign.getMax() != null)
			if (jsign.getMax() > 0) {
				sign.setLine(3,
						String.format("\u00A7e%s \u00A70left", jsign.getMax()));
			} else {
				sign.setLine(3, "\u00A7cQuota reached");
			}
		else
			sign.setLine(3, "");

		sign.update();
	}

	public String formatMaterial(JayconomySign jsign) {
		Material m = Material.getMaterial(jsign.getMaterialType());
		Short data = jsign.getSubtype();

		return formatMaterial(m, data);
	}

	public String formatMaterial(Material m, Short data) {
		if (data == null || data == 0) {
			String name = m.name();

			return name.toLowerCase().replaceAll("_", " ");
		} else {
			if (m == Material.LOG) {
				switch (data) {
				case 1:
					return "spruce";
				case 2:
					return "birch";
				default:
					return "tree";
				}
			}
			if (m == Material.LEAVES) {
				switch (data) {
				case 1:
					return "spruceleaves";
				case 2:
					return "birchleaves";
				default:
					return "leaves";
				}
			}
			if (m == Material.COAL) {
				switch (data) {
				case 1:
					return "charcoal";
				default:
					return "coal";
				}
			}
			if (m == Material.SAPLING) {
				switch (data) {
				case 1:
					return "spruceling";
				case 2:
					return "birchling";
				default:
					return "sapling";
				}
			}
			if (m == Material.WOOL) {
				switch (data) {
				case 1:
					return "orng wool";
				case 2:
					return "mag wool";
				case 3:
					return "lblu wool";
				case 4:
					return "yllw wool";
				case 5:
					return "lme wool";
				case 6:
					return "pnk wool";
				case 7:
					return "gry wool";
				case 8:
					return "lgry wool";
				case 9:
					return "cyan wool";
				case 10:
					return "prpl wool";
				case 11:
					return "blue wool";
				case 12:
					return "brwn wool";
				case 13:
					return "grn wool";
				case 14:
					return "rd wool";
				case 15:
					return "blck wool";
				default:
					return "white wool";
				}
			}
			if (m == Material.INK_SACK) {
				switch (data) {
				case 1:
					return "rose rd";
				case 2:
					return "ccts grn";
				case 3:
					return "cocoa bn";
				case 4:
					return "lps dye";
				case 5:
					return "prpl dye";
				case 6:
					return "cyan dye";
				case 7:
					return "lgry dye";
				case 8:
					return "gry dye";
				case 9:
					return "pnk dye";
				case 10:
					return "lm dye";
				case 11:
					return "dnd ylw";
				case 12:
					return "lbl dye";
				case 13:
					return "mag dye";
				case 14:
					return "orng dye";
				case 15:
					return "bone meal";
				default:
					return "ink sac";
				}
			}
			if (m == Material.STEP) {
				switch (data) {
				case 1:
					return "snd slb";
				case 2:
					return "wd slb";
				case 3:
					return "cbl slb";
				case 4:
					return "brk slb";
				case 5:
					return "stn brk slb";
				default:
					return "stn slb";
				}
			}
			if (m == Material.DOUBLE_STEP) {
				switch (data) {
				case 1:
					return "snd dslb";
				case 2:
					return "wd dslb";
				case 3:
					return "cbl dslb";
				case 4:
					return "brk dslb";
				case 5:
					return "stn brk dslb";
				default:
					return "stn dslb";
				}

			}
			if (m == Material.POTION) {
				switch (data) {
				case 0:
					return "water bottle";
				case 16:
					return "awkwrd pot";
				case 32:
					return "thick pot";
				case 64:
					return "ex mnd pot";
				case 8192:
					return "mnd pot";
				case 8193:
					return ".75m rgn pot";
				case 8257:
					return "2m rgn pot";
				case 8225:
					return ".2m rgn2 pot";
				case 8194:
					return "3m swft pot";
				case 8258:
					return "8m swft pot";
				case 8226:
					return "1.5m swft2 pot";
				case 8195:
					return "3m fr pot";
				case 8259:
					return "8m fr pot";
				case 8197:
					return "heal pot";
				case 8229:
					return "heal2 pot";
				case 8201:
					return "3m str pot";
				case 8265:
					return "8m str pot";
				case 8233:
					return "1.5m str2 pot";
				case 8196:
					return ".75m psn pot";
				case 8260:
					return "2m psn pot";
				case 8228:
					return ".2m psn2 pot";
				case 8200:
					return "1.5m weak pot";
				case 8264:
					return "4m weak pot";
				case 8202:
					return "1.5m slow pot";
				case 8266:
					return "4m slow pot";
				case 8204:
					return "harm pot";
				case 8236:
					return "harm2 pot";
				case 16384:
					return "mnd splot";
				case 16385:
					return "spl .5m rgn";
				case 16449:
					return "spl 1.5m rgn";
				case 16417:
					return "spl .25m rgn2";
				case 16386:
					return "spl 2.25m swft";
				case 16450:
					return "spl 6m swft";
				case 16418:
					return "spl 1m swft2";
				case 16387:
					return "spl 2.25m fr";
				case 16451:
					return "spl 6m fr";
				case 16389:
					return "spl heal";
				case 16421:
					return "spl heal2";
				case 16393:
					return "spl 2.25m str";
				case 16457:
					return "spl 6m str";
				case 16425:
					return "spl 1m str2";
				case 16388:
					return "spl .5m psn";
				case 16452:
					return "spl 1.5m psn";
				case 16420:
					return "spl .25m psn2";
				case 16392:
					return "spl 1m weak";
				case 16456:
					return "spl 3m weak";
				case 16394:
					return "spl 1m slow";
				case 16458:
					return "spl 3m slow";
				case 16396:
					return "spl harm";
				case 16428:
					return "spl harm2";
				}
			}
		}

		return m.getId() + "-" + data;
	}

	public ItemStack getSignStack(JayconomySign sign) {
		if (sign.getMaterialType() == Material.POTION.getId()) {
			return new ItemStack(sign.getMaterialType(), sign.getAmount(),
					sign.getSubtype(), null);
		} else {
			return new ItemStack(sign.getMaterialType(), sign.getAmount(),
					(short) 0, sign.getSubtype() != null ? sign.getSubtype()
							.byteValue() : null);
		}
	}

	public ItemStack getDealStack(JayconomyDeal deal) {
		if (deal.getMaterialType() == Material.POTION.getId()) {
			return new ItemStack(deal.getMaterialType(), deal.getAmount(),
					deal.getSubType(), null);
		} else {
			return new ItemStack(deal.getMaterialType(), deal.getAmount(),
					(short) 0, deal.getSubType() != null ? deal.getSubType()
							.byteValue() : null);
		}
	}

	public boolean hasBalance(Player player, int amount) {

		return hasBalance(player, new BigDecimal(amount));
	}

	private boolean hasBalance(Player player, BigDecimal amount) {
		return getBalance(player.getName()).compareTo(amount) >= 0;
	}

	public String formatCurrency(int amount) {
		return formatCurrency(new BigDecimal(amount));
	}

	public void decreaseBalance(String name, int price) {
		decreaseBalance(name, new BigDecimal(price));
	}
}
