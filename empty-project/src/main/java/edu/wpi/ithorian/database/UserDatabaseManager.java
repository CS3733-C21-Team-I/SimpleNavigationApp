package edu.wpi.ithorian.database;

import static edu.wpi.ithorian.user.User.Permission.*;
import static edu.wpi.ithorian.user.User.Role.*;

import edu.wpi.ithorian.user.User;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  /**
   * Gets a User for their given screenname
   *
   * <p>THIS REALLY IS NOT THE SECURE METHOD OF DOING ANYTHING We should fix this once we have our
   * next push
   *
   * @param screenName
   */
  public User getUserForScreenname(String screenName) {
    // TODO - implement UserDatabaseManager.getUserForId

    int userId;

    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet rs =
          statement.executeQuery(
              "SELECT * FROM HOSPITAL_USERS WHERE SCREENNAME='" + screenName + "'");

      if (!rs.next()) {
        // TODO error handling
        throw new IllegalArgumentException("Attempted to acess nonexistant user");
      }
      userId = rs.getInt("USER_ID");
    } catch (SQLException e) {
      // TODO Error logging
      e.printStackTrace();
      return null;
    }

    Set<User.Role> rolesSet = new HashSet<>();

    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet rs =
          statement.executeQuery(
              "SELECT HR.ROLE_NAME FROM USER_TO_ROLE INNER JOIN HOSPITAL_ROLES HR on HR.ROLE_ID = USER_TO_ROLE.ROLE_ID WHERE USER_ID="
                  + userId);

      while (rs.next()) {
        rolesSet.add(getRoleForDatabaseName(rs.getString("ROLE_NAME")));
      }

      if (!rolesSet.contains(User.Role.BASE)) {
        // TODO Error Logging
        throw new IllegalStateException(
            "Requested user: " + screenName + " does not contain role BASE");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Set<User.Permission> permissionSet = new HashSet<>();

    try {

      for (User.Role role : rolesSet) {
        Statement statement = databaseRef.getConnection().createStatement();
        ResultSet rs =
            statement.executeQuery(
                "SELECT RP.RESOURCE_NAME FROM ROLE_TO_PERMISSION INNER JOIN RESOURCE_PERMISSIONS RP on ROLE_TO_PERMISSION.RESOURCE_ID = RP.RESOURCE_ID WHERE ROLE_ID=(SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='"
                    + getDatabaseNameForRole(role)
                    + "')");

        permissionSet.add(getPermissionForDatabaseName(rs.getString("RESOURCE_NAME")));
      }
    } catch (SQLException e) {
      // TODO ERROR Logging
      e.printStackTrace();
    }

    return new User(userId, screenName, rolesSet, permissionSet);
  }

  /**
   * Gets a screename for a user id
   *
   * @param id
   * @return
   */
  public String getDisplayNameForId(int id) {
    // TODO - implement UserDatabaseManager.getDisplayNameForId
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet rs =
          statement.executeQuery("SELECT SCREENNAME FROM HOSPITAL_USERS WHERE USER_ID=" + id);
      if (!rs.next()) {
        throw new IllegalArgumentException("Attempted to acess records of nonexistant user");
      }
      return rs.getString("SCREENNAME");
    } catch (SQLException e) {
      // TODO error handling
      e.printStackTrace();
      return "ERROR";
    }
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

  /**
   * A hack probably want to implement custom typing?
   *
   * @param role the role to lookup
   * @return String coresponding to ROLE_NAME column in database
   */
  private String getDatabaseNameForRole(User.Role role) {
    switch (role) {
      case BASE:
        return "BASE";
      case ADMIN:
        return "ADMIN";
      case PATIENT:
        return "PATIENT";
      case VISITOR:
        return "VISITOR";
      case EMPLOYEE:
        return "EMPLOYEE";
      default:
        return "ERROR";
    }
  }

  /**
   * A hack probably want to implement custom typing?
   *
   * @param name the role to lookup
   * @return Role coresponding to ROLE_NAME column in database
   */
  private User.Role getRoleForDatabaseName(String name) {
    switch (name) {
      case "EMPLOYEE":
        return EMPLOYEE;
      case "ADMIN":
        return ADMIN;
      case "VISITOR":
        return VISITOR;
      case "PATIENT":
        return PATIENT;
      case "BASE":
        return BASE;
      default:
        new Exception("ERROR value found in HospitalRoles").printStackTrace();
        return null;
    }
  }

  /**
   * A hack probably want to implement custom typing?
   *
   * @param permission the role to lookup
   * @return String coresponding to ROLE_NAME column in database
   */
  private String getDatabaseNameForPermission(User.Permission permission) {
    switch (permission) {
      case EDIT_MAP:
        return "EDIT_MAP";
      case VIEW_TICKET:
        return "VIEW_TICKET";
      case REQUEST_TICKET:
        return "REQUEST_TICKET";
      case EDIT_ANNOUNCEMENTS:
        return "EDIT_ANNOUNCEMENTS";
      default:
        return "ERROR";
    }
  }

  /**
   * A hack probably want to implement custom typing?
   *
   * @param name the role to lookup
   * @return Role coresponding to ROLE_NAME column in database
   */
  private User.Permission getPermissionForDatabaseName(String name) {
    switch (name) {
      case "EDIT_MAP":
        return EDIT_MAP;
      case "VIEW_TICKET":
        return VIEW_TICKET;
      case "REQUEST_TICKET":
        return REQUEST_TICKET;
      case "EDIT_ANNOUNCEMENTS":
        return EDIT_ANNOUNCEMENTS;
      default:
        new Exception("ERROR value found in ResourcePermissions").printStackTrace();
        return null;
    }
  }
}
