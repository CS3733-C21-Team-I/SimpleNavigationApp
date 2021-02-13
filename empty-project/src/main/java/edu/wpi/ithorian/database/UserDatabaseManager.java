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
          "CREATE TABLE HOSPITAL_USERS"
              + "("
              + " user_ID    integer NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + " screenName varchar(45) NOT NULL ,"
              + "PRIMARY KEY (user_ID)"
              + ")");
    } catch (SQLException e) {
      System.out.println("Error generating User table");
      e.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "CREATE TABLE HOSPITAL_ROLES"
              + "("
              + " role_ID          integer NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + " role_Name        varchar(45) NOT NULL ,"
              + " role_Description varchar(500) NOT NULL ,"
              + "PRIMARY KEY (role_ID)"
              + ")");
    } catch (SQLException e) {
      System.out.println("Error generating Role table");
      e.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "CREATE TABLE RESOURCE_PERMISSIONS"
              + "("
              + " resource_ID     integer NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + " resource_Name        varchar(45) NOT NULL ,"
              + " resource_Description varchar(500) NOT NULL ,"
              + "PRIMARY KEY (resource_ID)"
              + ")");
    } catch (SQLException e) {
      System.out.println("Error generating Role table");
      e.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "CREATE TABLE  USER_TO_ROLE("
              + "user_ID        integer NOT NULL,"
              + "role_ID        integer NOT NULL,"
              + "PRIMARY KEY (user_ID, role_ID),"
              + "FOREIGN KEY (user_ID) REFERENCES HOSPITAL_USERS(user_ID),"
              + "FOREIGN KEY (role_ID) REFERENCES  HOSPITAL_ROLES(role_ID))");
    } catch (SQLException e) {
      System.out.println("Error generating userToRole");
      e.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "CREATE TABLE  ROLE_TO_PERMISSION("
              + "role_ID        integer NOT NULL,"
              + "resource_ID        integer NOT NULL,"
              + "PRIMARY KEY (role_ID, resource_ID),"
              + "FOREIGN KEY (role_ID) REFERENCES  HOSPITAL_ROLES(role_ID),"
              + "FOREIGN KEY (resource_ID) REFERENCES RESOURCE_PERMISSIONS(resource_ID))");
    } catch (SQLException e) {
      System.out.println("Error generating roleToPermission");
      e.printStackTrace();
    }
  }

  @Override
  protected void dropTables() {

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE USER_TO_ROLE ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE ROLE_TO_PERMISSION ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE HOSPITAL_USERS ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
      ex.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE HOSPITAL_ROLES ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
      ex.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE RESOURCE_PERMISSIONS ");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }
  }
}
