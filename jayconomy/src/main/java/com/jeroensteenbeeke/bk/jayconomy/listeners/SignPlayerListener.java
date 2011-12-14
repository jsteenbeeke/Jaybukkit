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
package com.jeroensteenbeeke.bk.jayconomy.listeners;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyDeal;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyMaterial;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomySign.SignMode;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyStash;

public class SignPlayerListener extends PlayerListener {
	private final Jayconomy plugin;

	private static final Logger log = Logger.getLogger("Minecraft");

	public SignPlayerListener(Jayconomy plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		List<JayconomyDeal> deals = plugin.getDatabase()
				.createQuery(JayconomyDeal.class).where()
				.eq("player", event.getPlayer().getName()).findList();

		for (JayconomyDeal deal : deals) {
			ItemStack stack = plugin.getDealStack(deal);

			if (giveToPlayer(stack, event.getPlayer(), event.getPlayer()
					.getName(), false)) {
				plugin.getDatabase().delete(deal);
			}
		}
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;

		Block b = event.getClickedBlock();

		if (b.getType() == Material.SIGN_POST
				|| b.getType() == Material.WALL_SIGN) {

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				JayconomySign sign = plugin.getDatabase()
						.createQuery(JayconomySign.class).where()
						.eq("x", b.getX()).eq("y", b.getY()).eq("z", b.getZ())
						.eq("world", b.getWorld().getName()).findUnique();

				if (sign != null) {
					if (sign.getSignMode() == SignMode.SELL) {
						if (sign.getMax() != null) {
							if (event.getPlayer().getName()
									.equals(sign.getOwner())
									&& sign.getMax() != null) {
								addToSign(event, sign);
							} else {
								buyFromPlayer(event, sign);
							}
						} else {
							buyFromServer(event, sign);
						}

						event.setCancelled(true);
					} else if (sign.getSignMode() == SignMode.DEPOSIT) {
						deposit(event, sign);

						event.setCancelled(true);
					} else if (sign.getSignMode() == SignMode.BUY) {
						if (!event.getPlayer().getName()
								.equals(sign.getOwner())) {
							sellToPlayer(event, sign);
							event.setCancelled(true);
						} else {
							Messages.send(event.getPlayer(),
									"&cYou cannot sell materials to your own signs");
						}
					}
				}

			}
		}
	}

	private void sellToPlayer(PlayerInteractEvent event, JayconomySign sign) {
		Inventory inventory = event.getPlayer().getInventory();

		Material m = Material.getMaterial(sign.getMaterialType());

		if (sign.getMax() == null || sign.getMax() > 0) {
			if (inventory.contains(m, sign.getAmount())) {
				ItemStack stack = plugin.getSignStack(sign);
				BigDecimal balance = plugin.getBalance(sign.getOwner());

				if (balance.compareTo(sign.getValue()) > 0) {
					Player player = plugin.getServer().getPlayerExact(
							sign.getOwner());
					inventory.removeItem(stack);

					giveToPlayer(stack, player, sign.getOwner(), true);

					plugin.decreaseBalance(sign.getOwner(), sign.getValue());
					plugin.increaseBalance(event.getPlayer().getName(),
							sign.getValue());

					if (sign.getMax() != null) {

						sign.setMax(sign.getMax() - 1);
						plugin.getDatabase().update(sign);

						plugin.updateBuySign((Sign) event.getClickedBlock()
								.getState(), sign);
					}

					Messages.send(event.getPlayer(), String.format(
							"&aSold &e%s %s&a to &e%s &afor &e%s", stack
									.getAmount(), plugin.formatMaterial(stack
									.getType(), stack.getData() != null ? stack
									.getData().getData() : null), sign
									.getOwner(), plugin.formatCurrency(sign
									.getValue())));
					log.info(String.format(
							"[Jayconomy] %s sold %s %s to %s for %s", event
									.getPlayer().getName(), stack.getAmount(),
							stack.getType(), sign.getOwner(), plugin
									.formatCurrency(sign.getValue())));
				} else {
					Messages.send(event.getPlayer(),
							"&cSign owner does not have enough money for this deal");
				}

			} else {
				event.setCancelled(true);
				Messages.send(event.getPlayer(), "&cYou do not have enough "
						+ m.name());
			}
		} else {
			event.setCancelled(true);
			Messages.send(event.getPlayer(), "&cMaximum of this sign reached");
		}

	}

	@SuppressWarnings("deprecation")
	private boolean giveToPlayer(ItemStack stack, Player player,
			String playerName, boolean makeDeal) {
		Material m = stack.getType();
		Byte data = stack.getData().getData();

		if (player != null) {
			PlayerInventory targetInventory = player.getInventory();

			if (targetInventory.firstEmpty() != -1) {
				targetInventory.addItem(stack);
				Messages.send(player, "Received " + stack.getAmount() + " "
						+ plugin.formatMaterial(m, data) + " from signs");
				player.updateInventory();
				return true;
			} else {
				int required = stack.getAmount();

				for (Entry<Integer, ? extends ItemStack> e : targetInventory
						.all(m).entrySet()) {
					ItemStack c = e.getValue();

					int free = c.getMaxStackSize() - c.getAmount();
					required -= free;
				}

				if (required <= 0) {
					targetInventory.addItem(stack);
					player.updateInventory();
					Messages.send(player, "Received " + stack.getAmount() + " "
							+ stack.getType().name() + " from signs");
					return true;
				} else {
					if (makeDeal) {
						JayconomyDeal deal = new JayconomyDeal();
						deal.setAmount(stack.getAmount());
						deal.setMaterialType(stack.getType().getId());
						deal.setPlayer(player.getName());
						deal.setSubType(data != 0 ? data : null);

						plugin.getDatabase().save(deal);
					}
				}
			}

		} else {
			if (makeDeal) {
				JayconomyDeal deal = new JayconomyDeal();
				deal.setAmount(stack.getAmount());
				deal.setMaterialType(stack.getType().getId());
				deal.setPlayer(playerName);
				deal.setSubType(data != 0 ? data : null);

				plugin.getDatabase().save(deal);
			}
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	private void addToSign(PlayerInteractEvent event, JayconomySign sign) {
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		ItemStack stack = plugin.getSignStack(sign);

		Material m = Material.getMaterial(sign.getMaterialType());

		if (sign.getMaterialType() == stack.getTypeId()
				&& compareStackTypes(sign.getSubtype(),
						stack.getData() != null ? stack.getData().getData()
								: null)) {

			if (hasEnough(inventory, stack)) {

				if (sign.getAmount() == 1) {
					if (stack.getDurability() < m.getMaxDurability()
							&& stack.getDurability() > 0) {
						log.info(String
								.format("Attempt to sell item with durability %s, which is less than max %s",
										stack.getDurability(),
										m.getMaxDurability()));

						Messages.send(event.getPlayer(),
								"&cYou can only add unused items");
						return;
					}
				}

				sign.setMax(sign.getMax() + 1);
				plugin.getDatabase().update(sign);
				inventory.removeItem(stack);
				player.updateInventory();

				Messages.send(
						event.getPlayer(),
						String.format("&aAdded &e%s %s &ato sign",
								sign.getAmount(), plugin.formatMaterial(sign)));

				log.info(String.format(
						"%s added %s %s to sign at (%s, %s, %s)",
						player.getName(), sign.getAmount(), m, sign.getX(),
						sign.getY(), sign.getZ()));
			} else {
				Messages.send(
						player,
						String.format(
								"&cYou do not have enough materials to add to this sign (required: &e%s&c, in inventory: &e%s&c)",
								sign.getAmount(), count(inventory, stack)));
			}
		} else {
			Messages.send(
					event.getPlayer(),
					String.format(
							"&cIncorrect material. You are holding &e%s&c, while this sign sells &e%s",
							plugin.formatMaterial(stack.getType(), stack
									.getData() != null ? stack.getData()
									.getData() : null), plugin
									.formatMaterial(sign)));
		}

		plugin.updateSellSign((Sign) event.getClickedBlock().getState(), sign);

	}

	private boolean hasEnough(PlayerInventory inventory, ItemStack signStack) {
		return count(inventory, signStack) >= signStack.getAmount();
	}

	private boolean compareStackTypes(MaterialData data, MaterialData data2) {
		return compareStackTypes(data != null ? data.getData() : null,
				data2 != null ? data2.getData() : null);
	}

	private boolean compareStackTypes(Byte a, Byte b) {
		Byte aa = a != null && a.byteValue() == 0 ? null : a;
		Byte bb = b != null && b.byteValue() == 0 ? null : b;

		if (aa == null && bb == null)
			return true;

		if (aa == null || bb == null)
			return false;

		return a.equals(b);
	}

	private int count(PlayerInventory inventory, ItemStack signStack) {
		ItemStack[] stacks = inventory.getContents();
		int total = 0;

		for (ItemStack s : stacks) {
			if (s != null && s.getTypeId() == signStack.getTypeId()) {
				if (compareStackTypes(s.getData(), signStack.getData())) {
					total += s.getAmount();
				}
			}
		}

		return total;
	}

	private void deposit(PlayerInteractEvent event, JayconomySign sign) {
		ItemStack item = event.getItem();

		if (item == null)
			return;

		JayconomyMaterial material = plugin.getDatabase()
				.createQuery(JayconomyMaterial.class).where()
				.eq("itemId", item.getTypeId()).findUnique();

		if (material == null) {
			Messages.send(event.getPlayer(), "&cThis item is not depositable");
			return;
		}

		PlayerInventory inventory = event.getPlayer().getInventory();

		ItemStack targetStack = new ItemStack(material.getItemId(),
				material.getStackSize());

		if (!hasEnough(inventory, targetStack)) {
			Messages.send(
					event.getPlayer(),
					String.format(
							"&cCannot deposit this item, minimum of &e%s &c is required",
							material.getStackSize()));
			return;
		}

		JayconomyStash stash = plugin.getDatabase()
				.createQuery(JayconomyStash.class).where()
				.eq("materialType", item.getTypeId()).findUnique();

		if (stash == null) {
			stash = new JayconomyStash();
			stash.setMaterialType(item.getTypeId());
			stash.setSupply(0);
			plugin.getDatabase().save(stash);
		}

		stash.setSupply(targetStack.getAmount() + stash.getSupply());
		plugin.getDatabase().update(stash);

		plugin.increaseBalance(event.getPlayer().getName(), material.getWorth());

		inventory.removeItem(targetStack);

		Messages.send(event.getPlayer(), String.format(
				"&aSold &e%s %s &afor &e%s", material.getStackSize(), plugin
						.formatMaterial(
								Material.getMaterial(stash.getMaterialType()),
								(byte) 0), plugin.formatCurrency(material
						.getWorth())));
	}

	private void buyFromPlayer(PlayerInteractEvent event, JayconomySign sign) {
		Player player = event.getPlayer();
		String playername = player.getName();

		if (sign.getMax() > 0) {
			BigDecimal balance = plugin.getBalance(player.getName());

			if (balance.compareTo(sign.getValue()) > 0) {
				plugin.decreaseBalance(playername, sign.getValue());
				plugin.increaseBalance(sign.getOwner(), sign.getValue());

				giveToPlayer(plugin.getSignStack(sign), player, playername,
						true);

				sign.setMax(sign.getMax() - 1);
				plugin.getDatabase().update(sign);

				plugin.updateSellSign(
						(Sign) event.getClickedBlock().getState(), sign);

				Messages.send(event.getPlayer(), String.format(
						"&aBought &e%s %s &afor &e%s", sign.getAmount(),
						plugin.formatMaterial(sign),
						plugin.formatCurrency(sign.getValue())));

				log.info(String.format(
						"%s bought %s %s from sign at (%s, %s, %s)", event
								.getPlayer().getName(), sign.getAmount(),
						plugin.formatMaterial(sign), sign.getX(), sign.getY(),
						sign.getZ()));
			} else {
				Messages.send(
						event.getPlayer(),
						String.format(
								"&cYou do not have enough money. You have &e%s&c, this item costs &e%s.",
								plugin.formatCurrency(balance),
								plugin.formatCurrency(sign.getValue())));
			}
		} else {
			Messages.send(event.getPlayer(), "&cNo items left in stock");
		}

	}

	@SuppressWarnings("deprecation")
	private void buyFromServer(PlayerInteractEvent event, JayconomySign sign) {
		// Check server stash
		JayconomyStash stash = plugin.getDatabase()
				.createQuery(JayconomyStash.class).where()
				.eq("materialType", sign.getMaterialType()).findUnique();

		JayconomyMaterial material = plugin.getDatabase()
				.createQuery(JayconomyMaterial.class).where()
				.eq("itemId", sign.getMaterialType()).findUnique();

		BigDecimal balance = plugin.getBalance(event.getPlayer().getName());

		if (balance.compareTo(material.getWorth()) > 0) {

			if (stash != null && material != null) {
				if (material.isSellable()) {
					if (stash.getSupply() > material.getStackSize()) {
						stash.setSupply(stash.getSupply()
								- material.getStackSize());
						plugin.getDatabase().update(stash);

						event.getPlayer().getInventory()
								.addItem(plugin.getSignStack(sign));

						event.getPlayer().updateInventory();

						Messages.send(event.getPlayer(), String.format(
								"&aBought &e%s %s &afor &e%s",
								material.getStackSize(),
								plugin.formatMaterial(sign),
								plugin.formatCurrency(material.getWorth())));

						plugin.decreaseBalance(event.getPlayer().getName(),
								material.getWorth());

						plugin.updateSellSign((Sign) event.getClickedBlock()
								.getState(), sign);
					} else {
						Messages.send(event.getPlayer(),
								"&cNo supply of this material");
						event.setCancelled(true);
					}

				} else {
					Messages.send(event.getPlayer(),
							"&cNo supply of this material");
					event.setCancelled(true);
				}
			} else {
				Messages.send(event.getPlayer(),
						"&cThis item is server-controlled and cannot be sold");
				event.setCancelled(true);
			}
		} else {
			Messages.send(
					event.getPlayer(),
					"&cNot enough funds, your balance is &e"
							+ plugin.formatCurrency(balance));
			event.setCancelled(true);
		}
	}

}
