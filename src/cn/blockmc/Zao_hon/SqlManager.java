package cn.blockmc.Zao_hon;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class SqlManager {
	private static final String CREATE_DECORATION_TABLE = "CREATE TABLE IF NOT EXISTS Decoration(Name VARCHAR(15),UUID VARCHAR(40),Decoration VARCHAR(30))";
	private static final String INSERT_PLAYER_DECORATION = "INSERT INTO Decoration VALUES(?,?,?)";
	private static final String SELECT_PLAYER_DECORATION = "SELECT Decoration FROM decoration WHERE UUID = ?";
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private ConnectionPool pool;
	// private Connection conn;
	// private Lobby plugin;

	public SqlManager(Lobby plugin) {
		// this.plugin = plugin;
		boolean ismysql = plugin.getConfig().getBoolean("Mysql.Enable");
		if (ismysql) {
			// try {
			// Class.forName(JDBC_DRIVER);
			String host = plugin.getConfig().getString("Mysql.host");
			String port = plugin.getConfig().getString("Mysql.port");
			String database = plugin.getConfig().getString("Mysql.database");
			String name = plugin.getConfig().getString("Mysql.username");
			String password = plugin.getConfig().getString("Mysql.password");
			// conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" +
			// port + "/" + database
			// + "?characterEncoding=utf8&useSSL=false&autoReconnect=true",
			// name, password);
			// } catch (ClassNotFoundException e) {
			// plugin.getLogger().info("JDBC驱动加载失败!");
			// } catch (SQLException e) {
			// plugin.getLogger().info("连接MYSQL数据库失败!");
			// e.printStackTrace();
			// }
			PoolConfig pconfig = new PoolConfig();
			pconfig.setDriverClassName(JDBC_DRIVER);
			pconfig.setJDBCUrl(
					"jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=utf8&useSSL=false&");
			pconfig.setUserName(name);
			pconfig.setPassword(password);
			pconfig.setTestQuery("SELECT NAME FROM decoration");
			pool = new ConnectionPool(pconfig);

		} else {
			PoolConfig pconfig = new PoolConfig();
			pconfig.setDriverClassName("org.sqlite.JDBC");
			pconfig.setJDBCUrl("jdbc:sqlite:" + plugin.getDataFolder() + "/Lobby.db");
			pconfig.setTestQuery("SELECT NAME FROM decoration");
			pool = new ConnectionPool(pconfig);
			// try {
			// Class.forName("org.sqlite.JDBC");
			// conn = DriverManager.getConnection("jdbc:sqlite:" +
			// plugin.getDataFolder() + "/Lobby.db");
			// } catch (ClassNotFoundException e) {
			// plugin.getLogger().info("SQLITE数据库驱动未找到!");
			// } catch (SQLException e) {
			// plugin.getLogger().info("连接SQLITE数据库失败!");
			// }
		}
		this.createTable();

		// try {
		// conn.prepareStatement(CREATE_DECORATION_TABLE).execute();
		// conn.prepareStatement(INSERT_PLAYER_DECORATION);
		// conn.prepareStatement(SELECT_PLAYER_DECORATION);
		//
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

	}

	public List<String> getPlayerDecoration(Player p) {
		List<String> list = new ArrayList<String>();
		try {
			Connection conn = pool.getConnection();
			PreparedStatement s = conn.prepareStatement(SELECT_PLAYER_DECORATION);
			s.setString(1, p.getUniqueId().toString());
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			pool.releaseConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void addPlayerDecoration(Player p, String decoration) {
		try {
			Connection conn = pool.getConnection();
			PreparedStatement s = conn.prepareStatement(INSERT_PLAYER_DECORATION);
			s.setString(1, p.getName());
			s.setString(2, p.getUniqueId().toString());
			s.setString(3, decoration);
			s.execute();
			pool.releaseConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void createTable() {
		try {
			Connection conn = pool.getConnection();
			PreparedStatement s = conn.prepareStatement(CREATE_DECORATION_TABLE);
			s.execute();
			pool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public void close() {
	// try {
	// if (conn != null && !conn.isClosed()) {
	// conn.close();
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	public void close() {
		pool.close();
	}
}
