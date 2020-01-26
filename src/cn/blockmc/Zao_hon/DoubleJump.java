package cn.blockmc.Zao_hon;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJump {
	// private int jumplevel;
	private Listener listener;
	private Sound sound;
	private Particle particle;

	// private LobbyManager plugin;

	private DoubleJump(Lobby plugin) {
		// this.plugin = plugin;
		int jumplevel = plugin.getConfig().getInt("DoubleJump.JumpLevel");
		sound = Sound.valueOf(plugin.getConfig().getString("DoubleJump.Sound"));
		particle = Particle.valueOf(plugin.getConfig().getString("DoubleJump.Particle"));
		listener = new Listener() {
			@EventHandler
			public void setFlight(PlayerMoveEvent e) {
				Player p = e.getPlayer();
				if (p.getGameMode() == GameMode.SURVIVAL && p.isOnGround() && !p.getAllowFlight()) {
					p.setAllowFlight(true);
				}
			}

			@EventHandler
			public void onToggleFlying(PlayerToggleFlightEvent e) {
				Player p = e.getPlayer();
				if (p.getGameMode() == GameMode.SURVIVAL) {
					e.setCancelled(true);
					p.setFlying(false);
					p.setAllowFlight(false);
					p.setVelocity(p.getLocation().getDirection().multiply(0.3 * jumplevel).setY(0.3 * jumplevel));
					p.getWorld().playSound(p.getLocation(), sound, 100, 100);
					p.getWorld().spawnParticle(particle, p.getLocation(), 100);
				}
			}
		};
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	public void close() {
		HandlerList.unregisterAll(listener);

	}

	public static DoubleJump reload(Lobby plugin) {
		doublejump.close();
		return doublejump = new DoubleJump(plugin);
	}

	private static DoubleJump doublejump = null;

	public static DoubleJump start(Lobby plugin) {
		if (doublejump == null)
			doublejump = new DoubleJump(plugin);
		return doublejump;
	}
}
