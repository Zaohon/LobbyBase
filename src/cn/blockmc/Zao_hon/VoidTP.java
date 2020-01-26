package cn.blockmc.Zao_hon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidTP {
	private Location location;
	private int height = 0;
	private CommandExecutor commandexc;
	private Listener listener;
	private Lobby plugin;

	public VoidTP(Lobby plugin) {
		this.plugin = plugin;
		String world = plugin.getConfig().getString("VoidTP.Location.world");
		this.location = world == null ? plugin.getServer().getWorlds().get(0).getSpawnLocation()
				: new Location(Bukkit.getWorld(world), plugin.getConfig().getDouble("VoidTP.Location.x"),
						plugin.getConfig().getDouble("VoidTP.Location.y"),
						plugin.getConfig().getDouble("VoidTP.Location.z"),
						Float.valueOf(plugin.getConfig().getString("VoidTP.Location.yaw")),
						Float.valueOf(plugin.getConfig().getString("VoidTP.Location.pitch")));

		this.height = plugin.getConfig().getInt("VoidTP.height");

		initListener();
		initCommandExc();

	};

	private void initCommandExc() {
		this.commandexc = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
				if (cmd.getName().equalsIgnoreCase("VoidTP")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage("该指令只有玩家可以使用");
						return true;
					}
					Player p = (Player) sender;
					if (args.length == 0) {
						p.teleport(location);
						return true;
					}
					if (!p.hasPermission("VoidTP.Admin")) {
						p.sendMessage("§c权限不足");
						return true;
					}
					if (args.length == 1) {
						if (args[0].equals("go")) {
							p.teleport(location);
							p.sendMessage("§d已传送到虚空保护点");
							return true;
						} else if (args[0].equals("set")) {
							Location loc = p.getLocation();
							location = loc;
							plugin.getConfig().set("VoidTP.Location.world", loc.getWorld().getName());
							plugin.getConfig().set("VoidTP.Location.x", loc.getX());
							plugin.getConfig().set("VoidTP.Location.y", loc.getY());
							plugin.getConfig().set("VoidTP.Location.z", loc.getZ());
							plugin.getConfig().set("VoidTP.Location.pitch", loc.getPitch());
							plugin.getConfig().set("VoidTP.Location.yaw", loc.getYaw());
							plugin.saveConfig();
							p.sendMessage("§d已设置虚空保护点");
							return true;
						}
					}

					return true;
				}
				return false;
			}
		};
		plugin.getCommand("VoidTP").setExecutor(commandexc);
	}

	private void initListener() {
		this.listener = new Listener() {
			@EventHandler
			public void voidTP(PlayerMoveEvent e) {
				if (e.getTo().getBlockY() <= height) {
					e.getPlayer().teleport(location);
					e.getPlayer().setFallDistance(0);
				}
			}
		};
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	public void close() {
		HandlerList.unregisterAll(listener);
		NMSUtils.unregisterCommand(plugin, "VoidTP");
	}

	// private static VoidTP voidtp = null;

	public void reload(Lobby plugin) {
		this.close();
		plugin.reloadConfig();

	}

	// public static VoidTP start(Lobby plugin) {
	// if (voidtp == null)
	// voidtp = new VoidTP(plugin);
	// return voidtp;
	// }

}