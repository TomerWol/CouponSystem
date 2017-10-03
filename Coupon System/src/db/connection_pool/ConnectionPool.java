package db.connection_pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A singleton class that handle's all Connections of this application with the
 * database.</br>
 * This class holds a SET of 10 connections that each user can take from for his
 * use and brings it back after he finishes.
 * 
 */
public class ConnectionPool {
	// Attributes
	private Set<Connection> connectionsToGive = new HashSet<>();
	private Set<Connection> connectionsToClose = new HashSet<>();
	private static ConnectionPool instance = null;
	private String url = "jdbc:derby://localhost:1527/coupon_system";
	private String DriverName = "org.apache.derby.jdbc.ClientDriver";

	// creating 10 connections and putting them in a SET.
	private ConnectionPool() {
		try {
			Class.forName(DriverName);
			for (int i = 0; i < 10; i++) {
				Connection con = DriverManager.getConnection(url);
				connectionsToGive.add(con);
				connectionsToClose.add(con);
			}
		} catch ( SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

	}

	// creating a singleton class.
	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	// Methods

	/**
	 * get connection method, if there is no connections left he will put you in
	 * wait until someone will return a connection.
	 */
	public synchronized Connection getConnection() {
		while (connectionsToGive.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		Iterator<Connection> it = connectionsToGive.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}

	/**
	 * the return connection method that return the connection to the pool and
	 * notify if there is someone that is waiting for connection.
	 * 
	 * @param con
	 *            the connection you'r returning.
	 */
	public synchronized void returnConnection(Connection con) {
		connectionsToGive.add(con);
		notify();
	}

	/**
	 * closing/shutting down all the connections in the pool.
	 */
	public synchronized void closeAllConnections() {
		for (int i = 0; i < connectionsToClose.size(); i++) {
			try {
				connectionsToClose.iterator().next().close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
