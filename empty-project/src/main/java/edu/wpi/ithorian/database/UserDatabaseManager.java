package edu.wpi.ithorian.database;

import edu.wpi.ithorian.user.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";
  private static UserDatabaseManager ourInstance;

  /**
   * Singleton init
   *
   * @param regen Set true if database should be wiped and reloaded from sources
   */
  public static void init(boolean regen) {
    ourInstance = new UserDatabaseManager(regen);
    // TODO reload from CSV
  }


  /**
   * Singleton access
   *
   * @return the singleton
   */
  public static UserDatabaseManager getInstance() {
    return ourInstance;
  }

  /**
   * Constructor for UserDatabase
   *
   * @param regen
   */
  private UserDatabaseManager(boolean regen) {
    super(DB_URL, regen);
    // TODO probably more stuff

  }

  /** @param userId */
  public User getUserForId(String userId) {
    // TODO - implement UserDatabaseManager.getUserForId
    throw new UnsupportedOperationException();
  }

  public String getDisplayNameForId() {
    // TODO - implement UserDatabaseManager.getDisplayNameForId
    throw new UnsupportedOperationException();
  }

  /** @param roleId */
  public List<String> getUsersWithRole(int roleId) {
    // TODO - implement UserDatabaseManager.getUsersWithRole
    throw new UnsupportedOperationException();
  }

  @Override
  protected void createTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
              "CREATE TABLE `hopital_Users`" +
                      "(" +
                      " `user_ID`    integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                      " `screenName` varchar(45) NOT NULL ," +
                      "PRIMARY KEY (`user_ID`)" +
                      ");");
    } catch (SQLException e) {
      System.out.println("Error generating User table");
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
              "CREATE TABLE `hospital_Roles`" +
                      "(" +
                      " `role_ID`          integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                      " `role_Name`        varchar(45) NOT NULL ," +
                      " `role_Description` varchar(500) NOT NULL ," +
                      "PRIMARY KEY (`role_ID`)\n" +
                      ");");
    } catch (SQLException e) {
      System.out.println("Error generating Map table");
    }
  }

  @Override
  protected void dropTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE hopital_Users ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE hopital_Roles ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }
  }
}
