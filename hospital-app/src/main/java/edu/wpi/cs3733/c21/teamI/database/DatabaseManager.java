package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import java.sql.SQLException;
import java.util.Map;
import org.apache.derby.drda.NetworkServerControl;

public abstract class DatabaseManager {

  DatabaseRef databaseRef;

  DatabaseManager(String url, boolean regen) {
    databaseRef = DatabaseRef.getConnection(url, regen);
  }

  abstract void createTables();

  abstract void dropTables();

  public static void startNetworkServer() {
    try {
      NetworkServerControl control = new NetworkServerControl();
      control.start(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void initDatabaseManagers(boolean regen) {
    ServiceTicketDatabaseManager.init(regen);
    UserDatabaseManager.init(regen);
    NavDatabaseManager.init(regen);
    NotificationManager.init(regen);
  }

  public static void initPeripheralDatabaseManagers(boolean regen) {
    ParkingPeripheralServerManager.init(regen);
  }

  public static void regenTables() {
    ServiceTicketDatabaseManager.getInstance().dropTables();
    NotificationManager.getInstance().dropTables();
    UserDatabaseManager.getInstance().dropTables();
    NavDatabaseManager.getInstance().dropTables();

    NavDatabaseManager.getInstance().createTables();
    UserDatabaseManager.getInstance().createTables();
    NotificationManager.getInstance().createTables();
    ServiceTicketDatabaseManager.getInstance().createTables();

// database potential path change
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            System.getProperty("user.dir") + "/MapINodes.csv",
            System.getProperty("user.dir") + "/MapIEdges.csv");

    NavDatabaseManager.getInstance().saveMapsIntoMemory(maps.values());
    UserDatabaseManager.populateExampleData();
    ServiceTicketDatabaseManager.populateExampleData();
    NavDatabaseManager.populateExampleData();
  }

  public static void regenPeripheralDB() {
    ParkingPeripheralServerManager.getInstance().dropTables();
    ParkingPeripheralServerManager.getInstance().createTables();

    ParkingPeripheralServerManager.getInstance().populateExampleData();
  }

  public static void printSQLException(SQLException e) {
    // According to this page:
    // https://db.apache.org/derby/papers/DerbyTut/embedded_intro.html
    // " A clean shutdown always throws SQL exception XJ015, which can be ignored."
    if (e.getSQLState().equals("XJ015")) return; // Ignore

    // Unwraps the entire exception chain to unveil the real cause of the
    // Exception.
    while (e != null) {
      System.err.println("\n----- SQLException -----");
      System.err.println("  SQL State:  " + e.getSQLState());
      System.err.println("  Error Code: " + e.getErrorCode());
      System.err.println("  Message:    " + e.getMessage());
      // for stack traces, refer to derby.log or uncomment this:
      e.printStackTrace(System.err);
      e = e.getNextException();
    }
  }
}
