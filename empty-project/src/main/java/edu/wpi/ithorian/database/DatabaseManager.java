package edu.wpi.ithorian.database;

public abstract class DatabaseManager {

  DatabaseRef databaseRef;

  DatabaseManager(String url, boolean regen) {
    databaseRef = DatabaseRef.getConnection(url, regen);

    if (regen) {
      dropTables();
      createTables();
    }
  }

  protected abstract void createTables();

  protected abstract void dropTables();
}
