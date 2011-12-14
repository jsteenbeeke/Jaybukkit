package com.jeroensteenbeeke.bk.jayconomy.listeners;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyMaterial;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign.SignMode;

public class SignBlockListener extends BlockListener {
	static final Pattern PATTERN_EMPTY = Pattern.compile("^$");

	static final Pattern PATTERN_DOLLAR = Pattern
			.compile("^\\$\\s*(\\d+(\\.\\d+)?)$");

	static final Pattern PATTERN_AMOUNT = Pattern.compile("(^\\d+$)");

	static final Pattern PATTERN_ITEMCODE = Pattern
			.compile("^(\\d+)(-(\\d+))?$");

	static final Pattern PATTERN_AMOUNT_PLUS_MAX = Pattern
			.compile("^(\\d+) max (\\d+)$");

	private static final Pattern[] PATTERN_BUY_MAX = {
			Pattern.compile("^\\[Buy\\]$"), PATTERN_ITEMCODE,
			PATTERN_AMOUNT_PLUS_MAX, PATTERN_DOLLAR };

	private static final Pattern[] PATTERN_BUY_NO_MAX = {
			Pattern.compile("^\\[Buy\\]$"), PATTERN_ITEMCODE, PATTERN_AMOUNT,
			PATTERN_DOLLAR };

	private static final Pattern[] PATTERN_SELL_SERVER = {
			Pattern.compile("^\\[Sell\\]$"), PATTERN_ITEMCODE, PATTERN_EMPTY,
			PATTERN_EMPTY };

	private static final Pattern[] PATTERN_SELL_PERSON = {
			Pattern.compile("^\\[Sell\\]$"), PATTERN_ITEMCODE, PATTERN_AMOUNT,
			PATTERN_DOLLAR };

	private static final Pattern[] PATTERN_DEPOSIT = {
			Pattern.compile("^\\[Deposit\\]$"), PATTERN_EMPTY, PATTERN_EMPTY,
			PATTERN_EMPTY };
	private final Set<BlockFace> adjacents;

	private final Jayconomy plugin;

	public SignBlockListener(Jayconomy plugin) {
		this.plugin = plugin;

		adjacents = new HashSet<BlockFace>();
		adjacents.add(BlockFace.NORTH);
		adjacents.add(BlockFace.EAST);
		adjacents.add(BlockFace.SOUTH);
		adjacents.add(BlockFace.WEST);
	}

	@Override
	public void onSignChange(SignChangeEvent event) {

		if (event.isCancelled())
			return;

		Block block = event.getBlock();

		if (block.getType() == Material.SIGN_POST
				|| block.getType() == Material.WALL_SIGN) {

			if (event.getLines().length > 0) {
				Player player = event.getPlayer();
				if (player != null) {
					if (matches(event, PATTERN_BUY_MAX)
							|| matches(event, PATTERN_BUY_NO_MAX)) {
						processBuySign(event);
					} else if (matches(event, PATTERN_SELL_SERVER)
							|| matches(event, PATTERN_SELL_PERSON)) {
						processSellSign(event);
					} else if (matches(event, PATTERN_DEPOSIT)) {
						processDepositSign(event);
					}
				}
			}
		}
	}

	private void processBuySign(SignChangeEvent event) {
		if (matches(event, PATTERN_BUY_MAX)) {
			Matcher m1 = PATTERN_ITEMCODE.matcher(event.getLine(1));
			Matcher amountMax = PATTERN_AMOUNT_PLUS_MAX.matcher(event
					.getLine(2));
			Matcher m2 = PATTERN_DOLLAR.matcher(event.getLine(3));

			m1.matches();
			m2.matches();
			amountMax.matches();

			int material = Integer.parseInt(m1.group(1));

			String sub = m1.group(3);
			Byte subcode = sub != null ? Byte.parseByte(sub) : null;
			BigDecimal value = new BigDecimal(m2.group(1));

			int amount = Integer.parseInt(amountMax.group(1));
			int max = Integer.parseInt(amountMax.group(2));

			Material m = Material.getMaterial(material);

			if (m != null) {
				createBuySign(event.getPlayer(), event, m, value, amount, max,
						subcode);
			} else {
				event.setCancelled(true);
				Messages.send(event.getPlayer(), "&cInvalid material type: &e"
						+ material);
			}

		}
		if (matches(event, PATTERN_BUY_NO_MAX)) {
			Matcher m1 = PATTERN_ITEMCODE.matcher(event.getLine(1));
			Matcher m3 = PATTERN_AMOUNT.matcher(event.getLine(2));
			Matcher m2 = PATTERN_DOLLAR.matcher(event.getLine(3));

			m1.matches();
			m2.matches();
			m3.matches();

			int material = Integer.parseInt(m1.group(1));
			BigDecimal value = new BigDecimal(m2.group(1));
			int amount = Integer.parseInt(m3.group(1));
			String sub = m1.group(3);
			Byte subcode = sub != null ? Byte.parseByte(sub) : null;

			Material m = Material.getMaterial(material);

			if (m != null) {
				createBuySign(event.getPlayer(), event, m, value, amount, 0,
						subcode);
			} else {
				event.setCancelled(true);
				Messages.send(event.getPlayer(), "&cInvalid material type: &e"
						+ material);
			}
		}
	}

	private void createBuySign(Player player, SignChangeEvent event,
			Material material, BigDecimal value, int amount, int max,
			Byte subcode) {
		event.setLine(0, "\u00A7a[ Buy ]\u00A70");
		event.setLine(
				1,
				String.format("\u00A7e%s %s", amount,
						plugin.formatMaterial(material, subcode)));
		event.setLine(2,
				String.format("\u00A7e%s", plugin.formatCurrency(value)));

		if (max > 0)
			event.setLine(3, String.format("\u00A7e%s left", max));
		else
			event.setLine(3, "");

		JayconomySign s = new JayconomySign();
		s.setAmount(amount);
		s.setMaterialType(material.getId());
		if (max > 0)
			s.setMax(max);
		s.setOwner(player.getName());
		s.setSignMode(SignMode.BUY);
		s.setValue(value);
		s.setWorld(event.getBlock().getWorld().getName());
		s.setX(event.getBlock().getX());
		s.setY(event.getBlock().getY());
		s.setZ(event.getBlock().getZ());
		s.setSubtype(subcode);

		plugin.getDatabase().save(s);

	}

	private void processDepositSign(SignChangeEvent event) {
		if (matches(event, PATTERN_DEPOSIT)) {
			event.setLine(0, "");
			event.setLine(1, "\u00A7a[ Deposit ]\u00A70");
			event.setLine(2, "");
			event.setLine(3, "");

			JayconomySign s = new JayconomySign();
			s.setMaterialType(null);
			s.setOwner(event.getPlayer().getName());
			s.setSignMode(SignMode.DEPOSIT);
			s.setWorld(event.getBlock().getWorld().getName());
			s.setX(event.getBlock().getX());
			s.setY(event.getBlock().getY());
			s.setZ(event.getBlock().getZ());

			plugin.getDatabase().save(s);
		}
	}

	private void processSellSign(SignChangeEvent event) {
		if (matches(event, PATTERN_SELL_PERSON)) {
			Matcher m1 = PATTERN_ITEMCODE.matcher(event.getLine(1));
			Matcher m2 = PATTERN_AMOUNT.matcher(event.getLine(2));
			Matcher m3 = PATTERN_DOLLAR.matcher(event.getLine(3));

			m1.matches();
			m2.matches();
			m3.matches();

			int material = Integer.parseInt(m1.group(1));
			int amount = Integer.parseInt(m2.group(1));
			BigDecimal value = new BigDecimal(m3.group(1));
			String sub = m1.group(3);
			Byte subcode = sub != null ? Byte.parseByte(sub) : null;

			Material m = Material.getMaterial(material);

			if (m != null) {

				JayconomyMaterial data = plugin.getDatabase()
						.createQuery(JayconomyMaterial.class).where()
						.eq("itemId", material).findUnique();

				if (data == null) {
					JayconomySign s = new JayconomySign();
					s.setAmount(amount);
					s.setMaterialType(material);
					s.setMax(0);
					s.setOwner(event.getPlayer().getName());
					s.setSignMode(SignMode.SELL);
					s.setValue(value);
					s.setWorld(event.getBlock().getWorld().getName());
					s.setX(event.getBlock().getX());
					s.setY(event.getBlock().getY());
					s.setZ(event.getBlock().getZ());
					s.setSubtype(subcode);

					plugin.getDatabase().save(s);

					event.setLine(0, "\u00A7a[ Sell ]\u00A70");
					event.setLine(
							1,
							String.format("\u00A7e%s %s", amount,
									plugin.formatMaterial(s)));
					event.setLine(
							2,
							String.format("\u00A7e%s ",
									plugin.formatCurrency(value)));
					event.setLine(3, "\u00A7cOut of stock");
				} else {
					event.setCancelled(true);
					Messages.send(event.getPlayer(), "&cMaterial &e" + m.name()
							+ "&c is server-controlled. ");
				}
			} else {
				Messages.send(event.getPlayer(), "&cInvalid material type: &e"
						+ material);
				event.setCancelled(true);
			}

		} else if (matches(event, PATTERN_SELL_SERVER)) {
			Matcher m1 = PATTERN_ITEMCODE.matcher(event.getLine(1));

			m1.matches();
			int material = Integer.parseInt(m1.group(1));
			String sub = m1.group(3);
			Byte subcode = sub != null ? Byte.parseByte(sub) : null;

			Material m = Material.getMaterial(material);

			if (m != null) {

				JayconomyMaterial data = plugin.getDatabase()
						.createQuery(JayconomyMaterial.class).where()
						.eq("itemId", material).findUnique();

				if (data != null) {
					JayconomySign s = new JayconomySign();
					s.setAmount(data.getStackSize());
					s.setMaterialType(material);
					s.setMax(null);
					s.setOwner(event.getPlayer().getName());
					s.setSignMode(SignMode.SELL);
					s.setValue(data.getWorth());
					s.setWorld(event.getBlock().getWorld().getName());
					s.setX(event.getBlock().getX());
					s.setY(event.getBlock().getY());
					s.setZ(event.getBlock().getZ());
					s.setSubtype(subcode);

					plugin.getDatabase().save(s);

					event.setLine(0, "\u00A7a[ Sell ]\u00A70");
					event.setLine(1, String.format("\u00A7e%s %s",
							data.getStackSize(), plugin.formatMaterial(s)));
					event.setLine(
							2,
							String.format("\u00A7e%s\u00A70",
									plugin.formatCurrency(data.getWorth())));
					event.setLine(3, "");
				} else {
					event.setCancelled(true);
					Messages.send(event.getPlayer(),
							"&cMaterial &e" + plugin.formatMaterial(m, subcode)
									+ "&c is not server-controlled. ");
				}
			} else {
				Messages.send(event.getPlayer(), "&cInvalid material type: &e"
						+ material);
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Block b = event.getBlock();

		if (b.getType() == Material.SIGN_POST
				|| b.getType() == Material.WALL_SIGN) {
			checkBreakPermission(event, b);
		} else {
			for (BlockFace f : adjacents) {
				Block r = b.getRelative(f);

				if (b.getType() == Material.SIGN_POST
						|| b.getType() == Material.WALL_SIGN) {
					checkBreakPermission(event, r);
				}
			}
		}
	}

	private void checkBreakPermission(BlockBreakEvent event, Block b) {
		JayconomySign sign = getSign(b);

		if (sign != null) {
			if (event.getPlayer() != null) {
				Player p = event.getPlayer();

				if (!p.getName().equals(sign.getOwner())) {
					event.setCancelled(true);
					Messages.send(p, "&cYou can only remove your own signs");
				} else {
					if (sign.getSignMode() != SignMode.SELL
							|| sign.getMax() == 0) {

						plugin.getDatabase().delete(sign);
					} else {
						event.setCancelled(true);
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	private JayconomySign getSign(Block b) {
		JayconomySign sign = plugin.getDatabase()
				.createQuery(JayconomySign.class).where().eq("x", b.getX())
				.eq("y", b.getY()).eq("z", b.getZ())
				.eq("world", b.getWorld().getName()).findUnique();
		return sign;
	}

	private boolean matches(SignChangeEvent event, Pattern[] pattern) {
		String[] lines = event.getLines();
		int len = lines.length;

		for (int i = 0; i < 4; i++) {
			if (i < len) {
				if (!pattern[i].matcher(lines[i]).matches()) {
					return false;
				}
			} else {
				if (pattern[i] != PATTERN_EMPTY) {
					return false;
				}
			}
		}

		return true;
	}

}
