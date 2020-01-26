package cn.blockmc.Zao_hon;

import java.io.File;
import java.io.InputStreamReader;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cn.BlockMC.Zao_hon.AnvilLogin;
import net.milkbowl.vault.economy.Economy;

public class Lobby extends JavaPlugin {
	private SqlManager sqlmanager;
	private Economy economy;
	private AnvilLogin anvillogin;
	private File menufile;
	private FileConfiguration menuconfig;
	private BungeeServer bungeeserver;
	private DoubleJump doublejump;
	private NoDrop noDrop;
	private ServerMenu servermenu;
	private VoidTP voidtp;
	private DecorationManager decorationmanager;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.reloadConfig();
		this.getLogger().info("--------Lobby-------");
		this.getLogger().info("Version: 0.1");
		this.getLogger().info("Author: Zao_hon");
		this.getLogger().info("感谢下载");
		this.getLogger().info("--------------------------");
		this.getServer().getPluginManager()
				.registerEvents(new ArmorListener(YamlConfiguration
						.loadConfiguration(new InputStreamReader(getResource("blocked.yml"))).getStringList("blocked")),
				this);
		this.getCommand("Lobby").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
				if (!cmd.getName().equalsIgnoreCase("lobby")) {
					return false;
				}
				if (args.length == 1) {
					if (args[0].equals("reload")) {
						if (sender.hasPermission("Lobby.Reload")) {
							reloadConfig();
							sender.sendMessage("§a重载完成");
							return true;
						} else {
							sender.sendMessage("权限不足");
							return true;
						}
					}
				}
				return true;
			}

		});
		this.loadDepends();
		this.setupEconomy();
		this.sqlmanager = new SqlManager(this);
		this.enableFunction();

	}

	@Override
	public void onDisable() {
		sqlmanager.close();
	}

	private void loadDepends() {
		Plugin anv = this.getServer().getPluginManager().getPlugin("AnvilLogin");
		if (anv != null) {
			this.getLogger().info("已加载依赖插件AnvilLogin");
			anvillogin = (AnvilLogin) anv;
		}

	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		this.getLogger().info("已加载依赖插件Vault");
		economy = rsp.getProvider();
		return economy != null;
	}

	private void createMenuFile() {
		menufile = new File(getDataFolder(), "servermenu.yml");
		if (!menufile.exists()) {
			this.saveResource("servermenu.yml", false);
		}
	}

	private void enableFunction() {
		this.voidtp = new VoidTP(this);
		if (getConfig().getBoolean("DoubleJump.Enable"))
			DoubleJump.start(this);
		if (getConfig().getBoolean("NoDrop.Enable"))
			NoDrop.start(this);
		if (getConfig().getBoolean("ServerMenu.Enable")) {
			this.createMenuFile();
			this.menuconfig = YamlConfiguration.loadConfiguration(menufile);
			this.bungeeserver = new BungeeServer(this);
			this.servermenu = new ServerMenu(this);
			BungeeUtil.load(this);
		}
		if (getConfig().getBoolean("Decoration.Enable")) {
			this.decorationmanager = new DecorationManager(this);
		}

	}
	public Double getPlayerEconomy(OfflinePlayer offp){
		if(economy==null){
			return 0d;
		}else{
			return economy.getBalance(offp);
		}
	}

	public BungeeServer getBungeeServer() {
		return bungeeserver;
	}

	public FileConfiguration getServerMenuConfig() {
		return menuconfig;
	}

	public VoidTP getVoidTP() {
		return voidtp;
	}

	public ServerMenu getServerMenu() {
		return servermenu;
	}

	public AnvilLogin getAnvilLogin() {
		return anvillogin;
	}
	public Economy getEconomy(){
		return economy;
	}

	public SqlManager getSqlManager() {
		return sqlmanager;
	}

	public DecorationManager getDecorationMnager() {
		return decorationmanager;
	}

}
