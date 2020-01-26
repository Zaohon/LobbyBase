package cn.blockmc.Zao_hon;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Aura {
	private Color[] colors;
	private Player player;
	private BukkitRunnable runnable;

	public Aura(Color... colors) {
		this.colors = colors;
		init();
	}

	public void start(Lobby plugin) {
		runnable.runTaskTimer(plugin, 0, 1);
	}

	public void stop() {
		runnable.cancel();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	private void init() {
		runnable = new BukkitRunnable() {

			@Override
			public void run() {
				for (int i = 0; i < 180; i++) {
					Location loc = player.getEyeLocation().add(Math.sin(Math.PI * i*2 / 180) / 1.5, 0.5,
							Math.cos(Math.PI * i*2 / 180) / 1.5);
					player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1,
							new Particle.DustOptions(colors[i % colors.length], 0.25f));
				}

			}

		};
	}

	public Color[] getColors() {
		return colors;
	}

	@Override
	public Aura clone() {
		return new Aura(colors);
	}

	public boolean isSimilar(Aura aura) {
		if (aura.getColors().equals(colors)) {
			return true;
		} else {
			return false;
		}
	}

}
