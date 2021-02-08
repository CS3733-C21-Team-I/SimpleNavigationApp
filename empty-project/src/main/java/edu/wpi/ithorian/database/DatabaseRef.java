package edu.wpi.ithorian.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseRef {

  private String dbUrl;

  private Connection connection;

  private DatabaseRef(String url) {
    this.dbUrl = url;

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException c) {
      // TODO Logging BIG error
    }
    try {
      connection = DriverManager.getConnection(dbUrl);
    } catch (SQLException e) {
      System.out.println("Failed to connect to Database");
      connection = null;
    }
  }

  /**
   * Disconnect this DatabaseRef from the relevant Apache Database Closing the connection, should be
   * called prior to deconstruction
   */
  public void disconnect() {
    try {
      connection.close();
    } catch (SQLException e) {
      // TODO logging / error handling
    }
    connection = null;
    openConnections.remove(dbUrl);
  }

  /**
   * HashMap containing all current open DatabaseRefs Used to prevent attempting to connect to the
   * same database twice
   */
  private static HashMap<String, DatabaseRef> openConnections;

  public static DatabaseRef getConnection(String url) {
    if (openConnections == null) openConnections = new HashMap<>();
    if (openConnections.containsKey(url)) {
      return openConnections.get(url);
    } else {
      DatabaseRef newRef = new DatabaseRef(url);
      openConnections.put(url, newRef);
      return newRef;
    }
  }
}
