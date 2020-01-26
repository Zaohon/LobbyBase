package cn.blockmc.Zao_hon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BungeeServer {
	private Lobby plugin;

	public BungeeServer(Lobby plugin) {
		this.plugin = plugin;
	}

	public BungeeServerInfo getInfo(String ip, int port) {
		Socket socket = new Socket();

		try {
			socket.connect(new InetSocketAddress(ip, port), 1 * 500);

			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());

			out.write(0xFE);

			String s = "";
			int b;
			while ((b = in.read()) != -1) {
				if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
					s = s + (char) b;
				}
			}

			String[] infos = s.split("¡ì");
			String motd = infos[0];
			int players = Integer.valueOf(infos[1]);
			int maxplayers = Integer.valueOf(infos[2]);

			socket.close();
			
			

			return new BungeeServerInfo(motd, players, maxplayers);
		} catch (IOException e) {
			return new BungeeServerInfo("",0,0);
		}
	}

	class BungeeServerInfo {
		private final String motd;
		private final int players;
		private final int maxplayers;

		public BungeeServerInfo(final String motd, final int players, final int maxplayers) {
			this.motd = motd;
			this.players = players;
			this.maxplayers = maxplayers;
		}

		public String getMotd() {
			return motd;
		}

		public int getPlayers() {
			return players;
		}

		public int getMaxPlayers() {
			return maxplayers;
		}
	}

}
