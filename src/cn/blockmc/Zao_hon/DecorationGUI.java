package cn.blockmc.Zao_hon;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DecorationGUI implements Listener {
	private Player player;
	private boolean destroy = true;
	private DecorationManager manager;
	private Inventory maininventory;
	private Inventory hatinventory;
	private Inventory clothinventory;
	private Inventory pantinventory;
	private Inventory bootinventory;
	private Inventory aurainventory;

	private List<String> hasdecorations;

	public DecorationGUI(DecorationManager manager, Player p) {
		this.manager = manager;
		this.player = p;

		maininventory = Bukkit.createInventory(null, 54, "§d装饰");
		hatinventory = Bukkit.createInventory(null, 54, "§d头饰");
		clothinventory = Bukkit.createInventory(null, 54, "§d衣服");
		pantinventory = Bukkit.createInventory(null, 54, "§d裤子");
		bootinventory = Bukkit.createInventory(null, 54, "§d鞋子");
		aurainventory = Bukkit.createInventory(null, 54, "光环");

		hasdecorations = manager.getPlugin().getSqlManager().getPlayerDecoration(p);

		this.updateMainInventory();
		this.updateHatInventory();
		this.updateClothInventory();
		this.updatePantInventory();
		this.updateBootInventory();
		this.updateHatInventory();
		this.updateAuraInventory();
		manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
	}

	public void openMainInventory() {
		destroy = false;
		player.openInventory(maininventory);
		destroy = true;
	}

	private void openHatInventory() {
		destroy = false;
		player.openInventory(hatinventory);
		destroy = true;
	}

	private void openClothInventory() {
		destroy = false;
		player.openInventory(clothinventory);
		destroy = true;
	}

	private void openPantInventory() {
		destroy = false;
		player.openInventory(pantinventory);
		destroy = true;
	}

	private void openBootInventory() {
		destroy = false;
		player.openInventory(bootinventory);
		destroy = true;
	}

	private void openAuraInventory() {
		destroy = false;
		player.openInventory(aurainventory);
		destroy = true;
	}

	// private void openHaloInventory() {
	// destroy = false;
	// player.openInventory(haloinventory);
	// destroy = true;
	// }

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getPlayer().equals(player)) {
			if (destroy)
				destroy();
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			ItemStack clicked = e.getCurrentItem();
			Player p = (Player) e.getWhoClicked();
			Inventory inv = e.getInventory();
			int slot = e.getSlot();

			if (p.equals(player)) {

				if (clicked == null) {
					return;
				}

				e.setCancelled(true);
				// Main Inventory
				if (inv.equals(maininventory)) {
					switch (slot) {
					case 10:
						openHatInventory();
						return;
					case 12:
						openClothInventory();
						return;
					case 14:
						openPantInventory();
						return;
					case 16:
						openBootInventory();
						return;
					case 29:
						openAuraInventory();
						return;
					case 49:
						LotteryGUI gui = LotteryGUI.getLotteryGUI(p);
						gui.open();
					default:
						return;
					}
				}
				// Hat Inventory
				else if (inv.equals(hatinventory)) {
					if (slot == 45) {
						openMainInventory();
						return;
					} else if (slot == 53) {
						openClothInventory();
						return;
					}

					if (clicked.containsEnchantment(Enchantment.DURABILITY)) {
						p.getInventory().setHelmet(null);
						updateHatInventory();
						return;
					}
					if (clicked.hasItemMeta() && clicked.getItemMeta().hasLore()) {
						if (clicked.getItemMeta().getLore().contains("§a已拥有")) {
							ItemStack hat = new ItemStack(clicked);
							ItemMeta meta = hat.getItemMeta();
							meta.setLore(null);
							hat.setItemMeta(meta);
							p.getInventory().setHelmet(hat);
							updateHatInventory();
							return;
						}
					}

				}
				// Cloth Inventory
				else if (inv.equals(clothinventory)) {
					if (slot == 45) {
						openHatInventory();
						return;
					} else if (slot == 53) {
						openPantInventory();
						return;
					}
					if (clicked.containsEnchantment(Enchantment.DURABILITY)) {
						p.getInventory().setChestplate(null);
						;
						updateClothInventory();
						return;
					}
					if (clicked.hasItemMeta() && clicked.getItemMeta().hasLore()) {
						if (clicked.getItemMeta().getLore().contains("§a已拥有")) {
							ItemStack cloth = new ItemStack(clicked);
							ItemMeta meta = cloth.getItemMeta();
							meta.setLore(null);
							cloth.setItemMeta(meta);
							p.getInventory().setChestplate(cloth);
							updateClothInventory();
							return;
						}
					}

				}
				// Pant Inventory
				else if (inv.equals(pantinventory)) {
					if (slot == 45) {
						openClothInventory();
						return;
					} else if (slot == 53) {
						openBootInventory();
						return;
					}
					if (clicked.containsEnchantment(Enchantment.DURABILITY)) {
						p.getInventory().setLeggings(null);
						;
						updatePantInventory();
						return;
					}
					if (clicked.hasItemMeta() && clicked.getItemMeta().hasLore()) {
						if (clicked.getItemMeta().getLore().contains("§a已拥有")) {
							ItemStack pants = new ItemStack(clicked);
							ItemMeta meta = pants.getItemMeta();
							meta.setLore(null);
							pants.setItemMeta(meta);
							p.getInventory().setLeggings(pants);
							updatePantInventory();
							return;
						}
					}
				}
				// Boot Inventory
				else if (inv.equals(bootinventory)) {
					if (slot == 45) {
						openPantInventory();
						return;
					} else if (slot == 53) {
						openMainInventory();
						return;
					}
					if (clicked.containsEnchantment(Enchantment.DURABILITY)) {
						p.getInventory().setBoots(null);
						updateBootInventory();
						return;
					}
					if (clicked.hasItemMeta() && clicked.getItemMeta().hasLore()) {
						if (clicked.getItemMeta().getLore().contains("§a已拥有")) {
							ItemStack boot = new ItemStack(clicked);
							ItemMeta meta = boot.getItemMeta();
							meta.setLore(null);
							boot.setItemMeta(meta);
							p.getInventory().setBoots(boot);
							updateBootInventory();
							return;
						}
					}
				}
				// Aura Inventory
				else if (inv.equals(aurainventory)) {
					if (slot == 45) {
						openMainInventory();
						return;
					}

					if (clicked.containsEnchantment(Enchantment.DURABILITY)) {
						manager.setPlayerAura(player, null);
						updateAuraInventory();
						return;
					}
					if (clicked.hasItemMeta() && clicked.getItemMeta().hasLore()) {
						if (clicked.getItemMeta().getLore().contains("§a已拥有")) {
							String l = clicked.getItemMeta().getLocalizedName();
							Aura aura = manager.getAura(l).clone();
							manager.setPlayerAura(player, aura);
							updateAuraInventory();
							return;
						}
					}

				}
			}

		}
	}

	private void updateMainInventory() {

		ItemStack hat = new ItemStack(Material.TURTLE_HELMET);
		ItemMeta hmeta = hat.getItemMeta();
		hmeta.setDisplayName("§a头饰");
		hat.setItemMeta(hmeta);
		maininventory.setItem(10, hat);

		ItemStack cloth = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta cmeta = cloth.getItemMeta();
		cmeta.setDisplayName("§a衣服");
		cloth.setItemMeta(cmeta);
		maininventory.setItem(12, cloth);

		ItemStack pant = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemMeta pmeta = pant.getItemMeta();
		pmeta.setDisplayName("§a裤子");
		pant.setItemMeta(pmeta);
		maininventory.setItem(14, pant);

		ItemStack boot = new ItemStack(Material.GOLDEN_BOOTS);
		ItemMeta bmeta = boot.getItemMeta();
		bmeta.setDisplayName("§a鞋子");
		boot.setItemMeta(bmeta);
		maininventory.setItem(16, boot);

		ItemStack halo = new ItemStack(Material.SUNFLOWER);
		ItemMeta hameta = halo.getItemMeta();
		hameta.setDisplayName("§e光环");
		halo.setItemMeta(hameta);
		maininventory.setItem(29, halo);

		ItemStack wing = new ItemStack(Material.ELYTRA);
		ItemMeta wmeta = wing.getItemMeta();
		wmeta.setDisplayName("§e翅膀");
		wing.setItemMeta(wmeta);
		maininventory.setItem(31, wing);

		ItemStack lottery = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta lmeta = lottery.getItemMeta();
		lmeta.setDisplayName("§d§l抽奖");
		lottery.setItemMeta(lmeta);
		maininventory.setItem(49, lottery);
	}

	private void updateHatInventory() {

		Iterator<ItemStack> it = manager.getHats().iterator();
		for (int i = 9; i < 54; i++) {
			if (i % 9 == 0 || (i + 1) % 9 == 0) {
				continue;
			}
			if (it.hasNext()) {
				ItemStack item = it.next().clone();
				ItemMeta meta = item.getItemMeta();
				ItemStack hat = player.getInventory().getHelmet();
				if (hasdecorations.contains(meta.getLocalizedName())) {
					meta.setLore(Arrays.asList("§a已拥有"));
				} else {
					meta.setLore(Arrays.asList("§8未拥有"));
				}
				item.setItemMeta(meta);
				if (hat != null && hat.getItemMeta().getLocalizedName().equals(meta.getLocalizedName())) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				hatinventory.setItem(i, item);
			} else {
				break;
			}
		}
		ItemStack gomaininv = new ItemStack(Material.ARROW);
		ItemMeta mmeta = gomaininv.getItemMeta();
		mmeta.setDisplayName("§d返回主菜单");
		gomaininv.setItemMeta(mmeta);
		hatinventory.setItem(45, gomaininv);

		ItemStack goclothinv = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta cmeta = goclothinv.getItemMeta();
		cmeta.setDisplayName("§d查看衣服");
		goclothinv.setItemMeta(cmeta);
		hatinventory.setItem(53, goclothinv);

	}

	private void updateClothInventory() {
		Iterator<ItemStack> it = manager.getCloths().iterator();
		for (int i = 9; i < 54; i++) {
			if (i % 9 == 0 || (i + 1) % 9 == 0) {
				continue;
			}
			if (it.hasNext()) {
				ItemStack item = it.next().clone();
				ItemMeta meta = item.getItemMeta();
				ItemStack chestplate = player.getInventory().getChestplate();
				if (hasdecorations.contains(meta.getLocalizedName())) {
					meta.setLore(Arrays.asList("§a已拥有"));
				} else {
					meta.setLore(Arrays.asList("§8未拥有"));
				}
				item.setItemMeta(meta);
				if (chestplate != null && chestplate.getItemMeta().getLocalizedName().equals(meta.getLocalizedName())) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				clothinventory.setItem(i, item);
			} else {
				break;
			}
		}
		ItemStack gohatinv = new ItemStack(Material.TURTLE_HELMET);
		ItemMeta hmeta = gohatinv.getItemMeta();
		hmeta.setDisplayName("§d查看头饰");
		gohatinv.setItemMeta(hmeta);
		clothinventory.setItem(45, gohatinv);

		ItemStack gopantinv = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemMeta pmeta = gopantinv.getItemMeta();
		pmeta.setDisplayName("§d查看裤子");
		gopantinv.setItemMeta(pmeta);
		clothinventory.setItem(53, gopantinv);
	}

	private void updatePantInventory() {
		Iterator<ItemStack> it = manager.getPants().iterator();
		for (int i = 9; i < 54; i++) {
			if (i % 9 == 0 || (i + 1) % 9 == 0) {
				continue;
			}
			if (it.hasNext()) {
				ItemStack item = it.next().clone();
				ItemMeta meta = item.getItemMeta();
				ItemStack pants = player.getInventory().getLeggings();
				if (hasdecorations.contains(meta.getLocalizedName())) {
					meta.setLore(Arrays.asList("§a已拥有"));
				} else {
					meta.setLore(Arrays.asList("§8未拥有"));
				}
				item.setItemMeta(meta);
				if (pants != null && pants.getItemMeta().getLocalizedName().equals(meta.getLocalizedName())) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				pantinventory.setItem(i, item);
			} else {
				break;
			}
		}
		ItemStack goclothinv = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta cmeta = goclothinv.getItemMeta();
		cmeta.setDisplayName("§d查看衣服");
		goclothinv.setItemMeta(cmeta);
		pantinventory.setItem(45, goclothinv);

		ItemStack gobootinv = new ItemStack(Material.GOLDEN_BOOTS);
		ItemMeta bmeta = gobootinv.getItemMeta();
		bmeta.setDisplayName("§d查看鞋子");
		gobootinv.setItemMeta(bmeta);
		pantinventory.setItem(53, gobootinv);
	}

	private void updateBootInventory() {
		Iterator<ItemStack> it = manager.getBoots().iterator();
		for (int i = 9; i < 54; i++) {
			if (i % 9 == 0 || (i + 1) % 9 == 0) {
				continue;
			}
			if (it.hasNext()) {
				ItemStack item = it.next().clone();
				ItemMeta meta = item.getItemMeta();
				ItemStack boots = player.getInventory().getBoots();
				if (hasdecorations.contains(meta.getLocalizedName())) {
					meta.setLore(Arrays.asList("§a已拥有"));
				} else {
					meta.setLore(Arrays.asList("§8未拥有"));
				}
				item.setItemMeta(meta);
				if (boots != null && boots.getItemMeta().getLocalizedName().equals(meta.getLocalizedName())) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				bootinventory.setItem(i, item);
			} else {
				break;
			}
		}
		ItemStack gopantinv = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemMeta pmeta = gopantinv.getItemMeta();
		pmeta.setDisplayName("§d查看裤子");
		gopantinv.setItemMeta(pmeta);
		bootinventory.setItem(45, gopantinv);

		ItemStack gomaininv = new ItemStack(Material.ARROW);
		ItemMeta mmeta = gomaininv.getItemMeta();
		mmeta.setDisplayName("§d返回主菜单");
		gomaininv.setItemMeta(mmeta);
		bootinventory.setItem(53, gomaininv);
	}

	private void updateAuraInventory() {
		Iterator<ItemStack> it = manager.getAuraItems().iterator();
		for (int i = 9; i < 54; i++) {
			if (i % 9 == 0 || (i + 1) % 9 == 0) {
				continue;
			}
			if (it.hasNext()) {
				ItemStack item = it.next().clone();
				ItemMeta meta = item.getItemMeta();
				String ln = meta.getLocalizedName();
				Aura pa = manager.getPlayerAura(player);
				if (hasdecorations.contains(ln)) {
					meta.setLore(Arrays.asList("§a已拥有"));
				} else {
					meta.setLore(Arrays.asList("§8未拥有"));
				}
				item.setItemMeta(meta);
				if (pa != null && pa.isSimilar(manager.getAura(ln))) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				aurainventory.setItem(i, item);
			} else {
				break;
			}
		}

		ItemStack gomaininv = new ItemStack(Material.ARROW);
		ItemMeta mmeta = gomaininv.getItemMeta();
		mmeta.setDisplayName("§d返回主菜单");
		gomaininv.setItemMeta(mmeta);
		aurainventory.setItem(45, gomaininv);
	}

	private void destroy() {
		maininventory.clear();
		hatinventory.clear();
		clothinventory.clear();
		pantinventory.clear();
		bootinventory.clear();
		aurainventory.clear();
		HandlerList.unregisterAll(this);
	}

}
