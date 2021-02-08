package edu.wpi.ithorian.database;

public class NavDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB;create=true";

  private static NavDatabaseManager ourInstance;

  public static NavDatabaseManager getInstance() {
    return ourInstance;
  }

  public static void init(boolean regen) {
    ourInstance = new NavDatabaseManager(regen);
  }

  private NavDatabaseManager(boolean regen) {
    super(DB_URL, regen);
  }

  void dropTables() {}

  void createTables() {}
git }
