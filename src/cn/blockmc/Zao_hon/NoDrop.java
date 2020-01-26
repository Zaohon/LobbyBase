package cn.blockmc.Zao_hon;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class NoDrop {
	private List<String> itemlist;
	private boolean all;
	private Listener listener;

	private NoDrop(Lobby plugin) {
		itemlist = plugin.getConfig().getStringList("NoDrop.List");
		all = plugin.getConfig().getBoolean("NoDrop.All");
		listener = new Listener() {
			@EventHandler
			public void onPlayerDrop(PlayerDropItemEvent e) {
				Player p = e.getPlayer();
				if (p.hasPermission("NoDrop.ignore")) {
					return;
				} else {
					if(all)e.setCancelled(true);
					ItemStack item = e.getItemDrop().getItemStack();
					if (itemlist.contains(item.getType().name())) {
						e.setCancelled(true);
					}
				}
			}
		};
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	public void close(){
		HandlerList.unregisterAll(listener);
	}

	private static NoDrop nodrop = null;
	public static NoDrop reload(Lobby plugin){
		nodrop.close();
		return nodrop = new NoDrop(plugin);
	}

	public static NoDrop start(Lobby plugin) {
		if (nodrop == null)
			nodrop = new NoDrop(plugin);
		return nodrop;
	}
	

}
