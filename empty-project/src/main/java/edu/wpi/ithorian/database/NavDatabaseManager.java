package edu.wpi.ithorian.database;

public class NavDatabaseManager {

    private static final String DB_URL = "jdbc:derby:memory:navDB;create=true";

    private static NavDatabaseManager ourInstance;

    public static NavDatabaseManager getInstance() {
        return ourInstance;
    }

    public static void init() {
        ourInstance = new NavDatabaseManager();
    }

    private DatabaseRef databaseRef;

    private NavDatabaseManager() {
        databaseRef = DatabaseRef.getConnection(DB_URL);
    }
}
