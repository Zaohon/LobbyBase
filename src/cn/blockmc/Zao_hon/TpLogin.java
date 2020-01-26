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
import org.bukkit.event.player.PlayerJoinEvent;

public class TpLogin {
	private Listener listener;
	private Location location;
	private CommandExecutor commandexc;

	private TpLogin(Lobby plugin) {

		String world = plugin.getConfig().getString("TpLogin.Location.world");
		if (world != null)
			this.location = new Location(Bukkit.getWorld(world), plugin.getConfig().getDouble("TpLogin.Location.x"),
					plugin.getConfig().getDouble("TpLogin.Location.y"),
					plugin.getConfig().getDouble("TpLogin.Location.z"));

		listener = new Listener() {
			@EventHandler
			public void onPlayerJoin(PlayerJoinEvent e) {
				Player p = e.getPlayer();
				if (location == null) {
					if (p.isOp()) {
						p.sendMessage("§d你还没设置入服传送点！");
					}
				} else {
					p.teleport(location);
				}
			}
		};
		commandexc = new CommandExecutor(){
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
				if(!cmd.getName().equalsIgnoreCase("tplogin")){
					return false;
				}
				if(!(sender instanceof Player)){
					sender.sendMessage("该指令只能在游戏中使用");
					return true;
				}
//				Player p = (Player) sender;
				
				return true;
			}
			
		};
		plugin.getCommand("Tplogin").setExecutor(commandexc);
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	public void close() {
		HandlerList.unregisterAll(listener);
	}

	private static TpLogin tplogin = null;

	public static TpLogin start(Lobby plugin) {
		if (tplogin == null)
			tplogin = new TpLogin(plugin);
		return tplogin;
	}

}
