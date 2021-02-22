package edu.wpi.cs3733.c21.teamI.database;

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

    UserDatabaseManager.populateExampleData();
  }
}
