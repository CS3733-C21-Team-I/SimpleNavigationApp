package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import java.util.Map;

public abstract class DatabaseManager {

  DatabaseRef databaseRef;

  DatabaseManager(String url, boolean regen) {
    databaseRef = DatabaseRef.getConnection(url, regen);
  }

  abstract void createTables();

  abstract void dropTables();

  public static void initDatabaseManagers(boolean regen) {
    ServiceTicketDatabaseManager.init(regen);
    UserDatabaseManager.init(regen);
    NavDatabaseManager.init(regen);
  }

  public static void regenTables() {
    ServiceTicketDatabaseManager.getInstance().dropTables();
    UserDatabaseManager.getInstance().dropTables();
    NavDatabaseManager.getInstance().dropTables();

    NavDatabaseManager.getInstance().createTables();
    UserDatabaseManager.getInstance().createTables();
    ServiceTicketDatabaseManager.getInstance().createTables();

    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV("csv/NewNodes.csv", "csv/NewEdges.csv");
    NavDatabaseManager.getInstance().saveMapsIntoMemory(maps.values());
    UserDatabaseManager.populateExampleData();
    ServiceTicketDatabaseManager.populateExampleData();
    NavDatabaseManager.populateExampleData();
  }
}
