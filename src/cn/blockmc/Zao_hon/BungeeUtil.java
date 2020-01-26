package cn.blockmc.Zao_hon;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeUtil implements PluginMessageListener {
	private Lobby plugin;

	private BungeeUtil(Lobby plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("PlayerCount")) {
//			String server = in.readUTF(); 
//			int playercount = in.readInt();
		}
	}

	public void teleportPlayerToServer(Player p, String servername) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(servername);
		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	private static BungeeUtil bungeeutil;

	public static void load(Lobby plugin) {
		bungeeutil = new BungeeUtil(plugin);
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", bungeeutil);
	}

	public static BungeeUtil Get() {
		return bungeeutil;
	}

}
