package cn.blockmc.Zao_hon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class LotteryGUI implements Listener {

	// private static ItemStack[] lotteryitems = new ItemStack[22];
	private static Random r = new Random();
	private static HashMap<LotterySlot, ItemStack> lotteryitems = new HashMap<LotterySlot, ItemStack>();
	private static HashMap<UUID, LotteryGUI> playerlottery = new HashMap<UUID, LotteryGUI>();
	private static ItemStack exit;
	private static ItemStack decorate;
	private static ItemStack drawonce;
	private static ItemStack drawagain;
	private static BukkitRunnable refreshrunnable;
	private static DecorationManager manager;
	private LotteryCursor cursor = new LotteryCursor();
	private HashMap<LotterySlot, ItemStack> items = new HashMap<LotterySlot, ItemStack>();
	private ItemStack viewmoney;
	private Player player;
	private Inventory inventory;
	private Lobby plugin;
	private Double money;
	private boolean drawing = false;

	private LotteryGUI(Player p) {
		this.player = p;
		this.plugin = manager.getPlugin();
		init();
	}

	private void init() {
		inventory = Bukkit.createInventory(null, 54, "§d§l抽奖");
		lotteryitems.forEach((slot, item) -> {
			items.put(slot, item);
			inventory.setItem(slot.getInventoryID(), item);
		});

		updateMoney();

//		ItemMeta vmeta = viewmoney.getItemMeta();
//		vmeta.setDisplayName("§a当前余额");
//		vmeta.setLore(Arrays.asList("§e§l" + money));
//		viewmoney.setItemMeta(vmeta);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		inventory.setItem(22, drawonce);
		inventory.setItem(44, viewmoney);
		inventory.setItem(45, decorate);
		inventory.setItem(53, exit);


	}

	public void open() {
		player.openInventory(inventory);
	}

	public void destroy() {
		inventory.clear();
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().equals(inventory)) {
			e.setCancelled(true);
			int slot = e.getSlot();
			switch (slot) {
			case 22:
				if (drawing)
					return;
				
				if(cursor.hasNext()){
					
				}
				
				if(money<plugin.getConfig().getInt("DecorationManager.LotteryCost")){
					player.sendMessage("&e你的&b方块币&e不够！");
					return;
				}
				
				plugin.getEconomy().withdrawPlayer(player, plugin.getConfig().getInt("DecorationManager.LotteryCost"));
				
				int time = r.nextInt(100);
				LotterySlot cacslot = cursor.caculateSlot(time);
				ItemStack reward = inventory.getItem(cacslot.getInventoryID());
				//
				plugin.getLogger().info(cacslot.name());
				plugin.getLogger().info(time + "");
				//

				// plugin.getSqlManager().addPlayerDecoration(player,
				// reward.getItemMeta().getLocalizedName());
				LotteryRunnable runnable = new LotteryRunnable(time, new LotteryTask() {
					@Override
					public void runtask() {
						ItemStack first = inventory.getItem(cursor.getSlot().getInventoryID());
						if (first != null && first.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
							first.removeEnchantment(Enchantment.DURABILITY);
						}
						ItemStack second = inventory.getItem(cursor.next().getInventoryID());
						second.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					}

					@Override
					public void canceltask() {
						drawing = false;
						cursor.remove();
						inventory.setItem(cursor.getSlot().getInventoryID(), null);
						player.sendMessage("你抽中了" + reward.getItemMeta().getLocalizedName());
						player.sendMessage("但我插件还没写完所以不给");
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 100,
								100);
						player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
						return;
					}

				});
				runnable.runTaskTimer(manager.getPlugin(), 0, 3);
				drawing = true;
				return;
			case 45:
				DecorationGUI gui = new DecorationGUI(manager, player);
				gui.openMainInventory();
				return;
			case 53:
				player.closeInventory();
				return;
			}
		}
	}

	private void updateMoney() {
		money = plugin.getPlayerEconomy(player);
		if (viewmoney == null) {
			viewmoney = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
		}
		ItemMeta meta = viewmoney.getItemMeta();
		meta.setDisplayName("§a当前余额");
		meta.setLore(Arrays.asList("§d§l" + money + "§b§l方块币"));
		viewmoney.setItemMeta(meta);
	}

	public static void init(DecorationManager manager) {
		LotteryGUI.manager = manager;

		exit = new ItemStack(Material.BIRCH_DOOR);
		ItemMeta emeta = exit.getItemMeta();
		emeta.setDisplayName("§c离开");
		exit.setItemMeta(emeta);

		decorate = new ItemStack(Material.ELYTRA);
		ItemMeta dmeta = decorate.getItemMeta();
		dmeta.setDisplayName("§d查看装饰");
		decorate.setItemMeta(dmeta);

		drawonce = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta dometa = drawonce.getItemMeta();
		dometa.setDisplayName("§d§l抽一次");
		dometa.setLore(Arrays.asList("§a消耗§e§l50§a方块币"));
		drawonce.setItemMeta(dometa);

		drawagain = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta dameta = drawagain.getItemMeta();
		dameta.setDisplayName("§d§l再抽一次");
		dameta.setLore(Arrays.asList("§a消耗§e§l50§a方块币"));
		drawonce.setItemMeta(dameta);

		Calendar c = Calendar.getInstance();
		int s = 86401 - c.get(Calendar.SECOND) - c.get(Calendar.MINUTE) * 60 - c.get(Calendar.HOUR_OF_DAY) * 3600;

		refreshLotteryItems(manager);
		refreshrunnable = new BukkitRunnable() {

			@Override
			public void run() {
				refreshLotteryItems(manager);
				playerlottery.forEach((k, v) -> v.destroy());
				playerlottery.clear();
			}
		};

		refreshrunnable.runTaskTimer(manager.getPlugin(), s, 86400 * 20);

	}

	public static LotteryGUI getLotteryGUI(Player player) {
		UUID id = player.getUniqueId();
		if (playerlottery.containsKey(id)) {
			return playerlottery.get(id);
		} else {
			LotteryGUI gui = new LotteryGUI(player);
			playerlottery.put(id, gui);
			return gui;
		}
	}

	private static void refreshLotteryItems(DecorationManager manager) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		manager.getHats().forEach(i -> items.add(i.clone()));
		manager.getCloths().forEach(i -> items.add(i.clone()));
		manager.getPants().forEach(i -> items.add(i.clone()));
		manager.getBoots().forEach(i -> items.add(i.clone()));
		manager.getAuraItems().forEach(i -> items.add(i.clone()));
		int temp = 22;
		int index = 0;
		for (int i = 0; i < temp; i++) {
			if (items.isEmpty())
				return;
			index = r.nextInt(items.size() - 1);
			lotteryitems.put(LotterySlot.getLotterySlot(i), items.get(index));
			items.remove(index);
		}
	}

	class LotteryCursor {
		private List<LotterySlot> slots = new ArrayList<LotterySlot>();
		private LotterySlot slot = LotterySlot.SLOT_0;

		public LotteryCursor() {
			for (int i = 0; i < LotterySlot.values().length; i++) {
				slots.add(LotterySlot.values()[i]);
			}
		}

		public boolean hasNext() {
			return slots.isEmpty();
		}

		public LotterySlot getSlot() {
			return slot;
		}

		public LotterySlot next() {
			int i = slot.getID();
			while (true) {
				i = i + 1 < 22 ? i + 1 : 0;
				LotterySlot nslot = LotterySlot.getLotterySlot(i);
				if (slots.contains(nslot)) {
					return slot = nslot;
				}
			}
		}

		public LotterySlot caculateSlot(int time) {
			LotterySlot cslot = slot;
			for (int i = 0; i < time; i++) {
				int j = cslot.getID();
				while (true) {
					j = j + 1 < 22 ? j + 1 : 0;
					LotterySlot nslot = LotterySlot.getLotterySlot(j);
					if (slots.contains(nslot)) {
						cslot = nslot;
						break;
					}
				}
			}
			return cslot;
		}

		public void setSlot(LotterySlot slot) {
			this.slot = slot;
		}

		public void remove() {
			slots.remove(slot);
		}
	}

	class LotteryRunnable extends BukkitRunnable {
		private LotteryTask task;
		private int time;
		private int index = 0;

		public LotteryRunnable(int time, LotteryTask task) {
			this.time = time;
			this.task = task;
		}

		@Override
		public void run() {
			// if(task.isDestroy()){
			// this.cancel();
			// return;
			// }
			if (index >= time) {
				task.canceltask();
				this.cancel();
			} else {
				task.runtask();
				index++;
			}
		}

	}

	abstract class LotteryTask {
		// private boolean destroy = false;

		public abstract void runtask();

		public abstract void canceltask();

		// public boolean isDestroy() {
		// return destroy;
		// }
		//
		// public void destroy() {
		// destroy = true;
		// }
	}

	enum LotterySlot {
		SLOT_0(0, 1), SLOT_1(1, 2), SLOT_2(2, 3), SLOT_3(3, 4), SLOT_4(4, 5), SLOT_5(5, 6), SLOT_6(6, 7), SLOT_7(7,
				16), SLOT_8(8, 25), SLOT_9(9, 34), SLOT_10(10, 43), SLOT_11(11, 52), SLOT_12(12, 51), SLOT_13(13,
						50), SLOT_14(14, 49), SLOT_15(15, 48), SLOT_16(16, 47), SLOT_17(17, 46), SLOT_18(18,
								37), SLOT_19(19, 28), SLOT_20(20, 19), SLOT_21(21, 10);
		private final int id;
		private final int inventoryid;

		LotterySlot(int id, int iid) {
			this.id = id;
			this.inventoryid = iid;
		}

		public static LotterySlot getLotterySlot(int id) {
			for (LotterySlot slot : LotterySlot.values()) {
				if (slot.getID() == id) {
					return slot;
				}
			}
			return null;
		}

		public int getID() {
			return id;
		}

		public int getInventoryID() {
			return inventoryid;
		}

	}
}
