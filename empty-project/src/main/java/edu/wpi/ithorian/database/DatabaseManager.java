package edu.wpi.ithorian.database;

public abstract class DatabaseManager {

    private DatabaseRef databaseRef;

    public DatabaseManager(String url, boolean regenerate) {
        if (regenerate) {
            dropTables();
            createTables();
            url = url + "create=true";
        }

        databaseRef = DatabaseRef.getConnection(url);
    }

     abstract void createTables();
     abstract void dropTables();
}
