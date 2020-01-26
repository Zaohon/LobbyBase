package cn.blockmc.Zao_hon;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import cn.blockmc.Zao_hon.BungeeServer.BungeeServerInfo;

public class ServerMenu implements Listener {
	private FileConfiguration config;
	private BukkitRunnable timer;
	private Inventory inventory;
	private ItemStack item;
	private Lobby plugin;

	private HashMap<String, BungeeServerInfo> serverinfo = new HashMap<String, BungeeServerInfo>();
	private HashMap<String, String> serverip = new HashMap<String, String>();
	private HashMap<String, Integer> serverport = new HashMap<String, Integer>();

	public ServerMenu(Lobby plugin) {
		this.plugin = plugin;
		this.config = plugin.getServerMenuConfig();

		int size = config.getInt("Settings.Size");
		if (size % 9 != 0 || size > 54) {
			plugin.getLogger().info("服务器传送菜单创建失败！大小必须是9的倍数");
			return;
		}
		String invname = config.getString("Settings.Name");
		inventory = Bukkit.createInventory(null, size, invname);

		int refreshtime = config.getInt("Settings.Refresh-time", 0);

		ConfigurationSection sec = config.getConfigurationSection("Servers");
		if (sec != null) {
			sec.getKeys(false).forEach(key -> {
				String[] str = sec.getString(key).split(":");
				serverip.put(key, str[0]);
				serverport.put(key, Integer.valueOf(str[1]));
			});
			timer = new BukkitRunnable() {
				@Override
				public void run() {
					serverip.keySet().forEach(key -> {
						serverinfo.put(key, plugin.getBungeeServer().getInfo(serverip.get(key), serverport.get(key)));
					});
					refreshInventory();
				}
			};
			timer.runTaskTimer(plugin, 0, refreshtime);
		}

		Material m = Material.valueOf(config.getString("Settings.Item.Material"));
		String name = config.getString("Settings.Item.Name");
		List<String> lores = config.getStringList("Settings.Item.Lores");
		item = new ItemStack(m);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		if (!lores.isEmpty())
			meta.setLore(lores);
		item.setItemMeta(meta);

		plugin.getServer().getPluginManager().registerEvents(this, plugin);

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!p.getInventory().contains(item))
			p.getInventory().setItem(0, item);
	}

	@EventHandler
	public void onPlayerInteractItem(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (plugin.getAnvilLogin() != null && !plugin.getAnvilLogin().getLoggedPlayer().contains(p.getUniqueId())) {
			return;
		}
		if (p.getInventory().getItemInMainHand().equals(item)) {
			p.openInventory(inventory);
		}
	}

	@EventHandler
	public void onPlayerInteractInventory(InventoryClickEvent e) {
		if(e.isCancelled())return;
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			ItemStack clicked = e.getCurrentItem();
			if (plugin.getAnvilLogin() != null && !plugin.getAnvilLogin().getLoggedPlayer().contains(p.getUniqueId())) {
				return;
			}
			if (clicked != null && clicked.equals(item)) {
				e.setCancelled(true);
				p.openInventory(inventory);
				return;
			}
			if (e.getInventory().equals(inventory)) {
				e.setCancelled(true);
				if (clicked != null && clicked.hasItemMeta()) {
					String server = clicked.getItemMeta().getLocalizedName();
					BungeeUtil.Get().teleportPlayerToServer(p, server);
				}
			}
		}
	}

	private void refreshInventory() {
		ConfigurationSection sec = config.getConfigurationSection("Menu");
		if (sec != null) {
			sec.getKeys(false).forEach(key -> {
				String name = sec.getString(key + ".Name");
				int position = sec.getInt(key + ".Position");
				List<String> lores = sec.getStringList(key + ".Lores");
				String server = sec.getString(key + ".To-Server");
				Material m = Material.valueOf(sec.getString(key + ".Material"));
				ItemStack item = new ItemStack(m);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(name);
				meta.setLocalizedName(server);
				if (!lores.isEmpty()) {
					BungeeServerInfo info = serverinfo.get(server);
					for (int i = 0; i < lores.size(); i++) {
						String l = lores.get(i);
						l = l.replaceAll("%players%", info.getPlayers() + "").replaceAll("%maxplayers%",
								info.getMaxPlayers() + "");
						lores.set(i, l);
					}
					meta.setLore(lores);
					item.setItemMeta(meta);

					inventory.setItem(position, item);

				}
			});
		}
	}

}
