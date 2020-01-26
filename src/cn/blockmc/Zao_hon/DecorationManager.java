package cn.blockmc.Zao_hon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class DecorationManager implements Listener {
	private FileConfiguration config;
	private ItemStack decorateitem;
	private List<ItemStack> hats = new ArrayList<ItemStack>();
	private List<ItemStack> cloths = new ArrayList<ItemStack>();
	private List<ItemStack> pants = new ArrayList<ItemStack>();
	private List<ItemStack> boots = new ArrayList<ItemStack>();
	private List<ItemStack> auraitems = new ArrayList<ItemStack>();
	private HashMap<String, Aura> auras = new HashMap<String, Aura>();
	private HashMap<UUID, Aura> playeraura = new HashMap<UUID, Aura>();
	private Lobby plugin;

	public DecorationManager(Lobby plugin) {
		this.plugin = plugin;

		File file = new File(plugin.getDataFolder(), "decorations.yml");
		if (!file.exists()) {
			plugin.saveResource("decorations.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
		this.loadDecorations();

		decorateitem = new ItemStack(Material.ELYTRA);
		ItemMeta dmeta = decorateitem.getItemMeta();
		dmeta.setDisplayName("¡ìd×°ÊÎ");
		decorateitem.setItemMeta(dmeta);

		LotteryGUI.init(this);

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!p.getInventory().contains(decorateitem))
			p.getInventory().setItem(5, decorateitem);
	}

	@EventHandler
	public void onPlayerEquip(ArmorEquipEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (plugin.getAnvilLogin() != null && !plugin.getAnvilLogin().getLoggedPlayer().contains(p.getUniqueId())) {
			return;
		}
		if (e.getItem() != null && e.getItem().equals(decorateitem)) {
			DecorationGUI gui = new DecorationGUI(this, p);
			gui.openMainInventory();
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerClickedInv(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (plugin.getAnvilLogin() != null && !plugin.getAnvilLogin().getLoggedPlayer().contains(p.getUniqueId())) {
				return;
			}
			ItemStack clicked = e.getCurrentItem();
			if (clicked != null && clicked.equals(decorateitem)) {
				e.setCancelled(true);
				DecorationGUI gui = new DecorationGUI(this, p);
				gui.openMainInventory();
			}

		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		this.setPlayerAura(p, null);
	}

	public void setPlayerAura(Player p, Aura aura) {
		if (playeraura.containsKey(p.getUniqueId())) {
			playeraura.get(p.getUniqueId()).stop();
			playeraura.remove(p.getUniqueId());
		}
		if (aura != null) {
			playeraura.put(p.getUniqueId(), aura);
			aura.setPlayer(p);
			aura.start(plugin);
		}
	}
	//

	private void loadDecorations() {

		ConfigurationSection s1 = config.getConfigurationSection("Hats");
		if (s1 != null) {
			s1.getKeys(false).forEach(k -> {
				String n = s1.getString(k + ".Name");
				Material m = Material.valueOf(s1.getString(k + ".Material"));
				ItemStack item = new ItemStack(m);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(n);
				meta.setLocalizedName(k);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				if (m == Material.LEATHER_HELMET) {
					LeatherArmorMeta leathermeta = (LeatherArmorMeta) meta;
					int r = s1.getInt(k + ".Color.R");
					int g = s1.getInt(k + ".Color.G");
					int b = s1.getInt(k + ".Color.B");
					Color color = Color.fromRGB(r, g, b);
					leathermeta.setColor(color);
				}
				item.setItemMeta(meta);
				hats.add(item);
			});

		}

		ConfigurationSection s2 = config.getConfigurationSection("Cloths");
		if (s2 != null) {
			s2.getKeys(false).forEach(k -> {
				String n = s2.getString(k + ".Name");
				Material m = Material.valueOf(s2.getString(k + ".Material"));
				ItemStack item = new ItemStack(m);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(n);
				meta.setLocalizedName(k);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				if (m == Material.LEATHER_CHESTPLATE) {
					LeatherArmorMeta leathermeta = (LeatherArmorMeta) meta;
					int r = s2.getInt(k + ".Color.R");
					int g = s2.getInt(k + ".Color.G");
					int b = s2.getInt(k + ".Color.B");
					Color color = Color.fromRGB(r, g, b);
					leathermeta.setColor(color);
				}
				item.setItemMeta(meta);
				cloths.add(item);

			});
		}

		ConfigurationSection s3 = config.getConfigurationSection("Pants");
		if (s3 != null) {
			s3.getKeys(false).forEach(k -> {
				String n = s3.getString(k + ".Name");
				Material m = Material.valueOf(s3.getString(k + ".Material"));
				ItemStack item = new ItemStack(m);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(n);
				meta.setLocalizedName(k);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				if (m == Material.LEATHER_LEGGINGS) {
					LeatherArmorMeta leathermeta = (LeatherArmorMeta) meta;
					int r = s3.getInt(k + ".Color.R");
					int g = s3.getInt(k + ".Color.G");
					int b = s3.getInt(k + ".Color.B");
					Color color = Color.fromRGB(r, g, b);
					leathermeta.setColor(color);
				}
				item.setItemMeta(meta);
				pants.add(item);
			});
		}

		ConfigurationSection s4 = config.getConfigurationSection("Boots");
		if (s4 != null) {
			s4.getKeys(false).forEach(k -> {
				String n = s4.getString(k + ".Name");
				Material m = Material.valueOf(s4.getString(k + ".Material"));
				ItemStack item = new ItemStack(m);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(n);
				meta.setLocalizedName(k);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				if (m == Material.LEATHER_BOOTS) {
					LeatherArmorMeta leathermeta = (LeatherArmorMeta) meta;
					int r = s4.getInt(k + ".Color.R");
					int g = s4.getInt(k + ".Color.G");
					int b = s4.getInt(k + ".Color.B");
					Color color = Color.fromRGB(r, g, b);
					leathermeta.setColor(color);
				}
				item.setItemMeta(meta);
				boots.add(item);
			});
		}

		ConfigurationSection asec = config.getConfigurationSection("Auras");
		if (asec != null) {
			asec.getKeys(false).forEach(k -> {
				String n = asec.getString(k + ".Name");
				Material m = Material.valueOf(asec.getString(k + ".Material"));
				ItemStack item = new ItemStack(m);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(n);
				meta.setLocalizedName(k);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				item.setItemMeta(meta);
				auraitems.add(item);
				List<Color> colors = new ArrayList<Color>();
				asec.getConfigurationSection(k + ".AuraColors").getKeys(false).forEach(kk -> {
					String s = k + ".AuraColors." + kk;
					colors.add(Color.fromRGB(asec.getInt(s + ".R"), asec.getInt(s + ".G"), asec.getInt(s + ".B")));
				});

				auras.put(k, new Aura(colors.toArray(new Color[colors.size()])));
			});
		}

	}

	public Lobby getPlugin() {
		return plugin;
	}

	public List<ItemStack> getHats() {
		return hats;
	}

	public List<ItemStack> getCloths() {
		return cloths;
	}

	public List<ItemStack> getPants() {
		return pants;
	}

	public List<ItemStack> getBoots() {
		return boots;
	}

	public List<ItemStack> getAuraItems() {
		return auraitems;
	}

	public Aura getPlayerAura(Player p) {
		return playeraura.get(p.getUniqueId());
	}

	public Aura getAura(String s) {
		return auras.get(s);
	}

}
