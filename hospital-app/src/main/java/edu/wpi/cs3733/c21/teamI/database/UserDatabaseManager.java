package edu.wpi.cs3733.c21.teamI.database;

import static edu.wpi.cs3733.c21.teamI.user.User.Permission.*;
import static edu.wpi.cs3733.c21.teamI.user.User.Role.*;

import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

        while (rs.next()) {
          permissionSet.add(User.Permission.valueOf(rs.getString("RESOURCE_NAME")));
        }
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
  void createTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "CREATE TABLE HOSPITAL_USERS"
              + "("
              + " user_ID    integer NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + " screenName varchar(30) NOT NULL ,"
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

    //    try {
    //      Statement stmt = databaseRef.getConnection().createStatement();
    //      stmt.execute(
    //          "CREATE TABLE HOSPITAL_EMPLOYEE"
    //              + "("
    //              + " employeeID    integer NOT NULL,"
    //              + " firstName varchar(20) NOT NULL ,"
    //              + " lastName  varchar(20) NOT NULL,"
    //              + "FOREIGN KEY (employeeID) REFERENCES HOSPITAL_USERS(user_ID))");
    //    } catch (SQLException e) {
    //      System.out.println("Error generating User table");
    //      e.printStackTrace();
    //    }

  }

  @Override
  void dropTables() {

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE HOSPITAL_EMPLOYEE");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE USER_TO_ROLE");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE ROLE_TO_PERMISSION");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE HOSPITAL_USERS");
    } catch (SQLException ex) {
      ex.printStackTrace();
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE HOSPITAL_ROLES");
    } catch (SQLException ex) {
      // No need to report an error.
      // The table simply did not exist.
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      // Drop the Edges table.
      stmt.execute("DROP TABLE RESOURCE_PERMISSIONS");
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

  public List<String> getUsernamesWithPermission(User.Permission permission) {
    try {
      List<String> returnValues = new ArrayList<>();
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet rs =
          statement.executeQuery(
              "SELECT SCREENNAME FROM HOSPITAL_USERS JOIN USER_TO_ROLE UTR on HOSPITAL_USERS.USER_ID = UTR.USER_ID WHERE UTR.ROLE_ID=(SELECT ROLE_ID FROM ROLE_TO_PERMISSION JOIN RESOURCE_PERMISSIONS RP on RP.RESOURCE_ID = ROLE_TO_PERMISSION.RESOURCE_ID WHERE RP.RESOURCE_NAME='"
                  + permission.toString()
                  + "')");
      while (rs.next()) {
        returnValues.add(rs.getString("SCREENNAME"));
      }
      return returnValues;
    } catch (SQLException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public void addUser() {}

  public static void populateExampleData() {
    try {
      Statement stmt = ourInstance.databaseRef.getConnection().createStatement();
      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestVisitor')");
      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestEmployee')");
      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestServiceEmployee')");
      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestAdmin')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
              + "('SecurityManager'),"
              + "('SecurityNightManager'),"
              + "('SecurityGaurd')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
              + "('Laundry Employee'),"
              + "('Laundry Manager')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
              + "('Day Janitor'),"
              + "('Night Janitor'),"
              + "('Knight Janitor')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
              + "('Head of Maintenance'),"
              + "('Maintenance Tech')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
              + "('Jen Barber'),"
              + "('Roy Trenneman'),"
              + "('Maurice Moss')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
              + "('Spanish Translator'),"
              + "('Canadian Translator'),"
              + "('Mandarin Translator')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS(SCREENNAME) VALUES "
              + "('Nurse Joy'),"
              + "('Nurse Ratched'),"
              + "('Nurse Cramer'),"
              + "('Nurse Hot Lips Houlihan')");

      stmt.addBatch(
          "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES "
              + "('BASE', 'TODO'),"
              + "('ADMIN', 'TODO'),"
              + "('VISITOR', 'TODO'),"
              + "('EMPLOYEE', 'TODO'),"
              + "('PATIENT', 'TODO'),"
              + "('SECURITY_EMPLOYEE', 'TODO'),"
              + "('LAUNDRY_EMPLOYEE', 'TODO'),"
              + "('SANITATION_EMPLOYEE', 'TODO'),"
              + "('MAINTENANCE_EMPLOYEE', 'TODO'),"
              + "('IT_EMPLOYEE', 'TODO'),"
              + "('TRANSLATOR', 'TODO'),"
              + "('NURSE', 'TODO')");

      stmt.addBatch(
          "INSERT INTO RESOURCE_PERMISSIONS (RESOURCE_NAME, RESOURCE_DESCRIPTION) VALUES "
              + "('EDIT_MAP', 'Place and Edit nodes on the hospital map'),"
              + "('VIEW_TICKET', 'TODO'),"
              + "('REQUEST_TICKET', 'TODO'),"
              + "('EDIT_ANNOUNCEMENTS', 'TODO'),"
              + "('REQUEST_PARKING_PERMIT', 'TODO'),"
              + "('RESPOND_TO_SECURITY', 'TODO'),"
              + "('RESPOND_TO_LAUNDRY', 'TODO'),"
              + "('RESPOND_TO_SANITATION', 'TODO'),"
              + "('RESPOND_TO_MAINTENANCE', 'TODO'),"
              + "('RESPOND_TO_AV', 'TODO'),"
              + "('RESPOND_TO_COMPUTER', 'TODO'),"
              + "('RESPOND_TO_TRANSLATOR', 'TODO'),"
              + "('RESPOND_TO_MEDICINE_REQUEST', 'TODO')");

      stmt.addBatch(
          "INSERT INTO USER_TO_ROLE (USER_ID, ROLE_ID) VALUES "
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestVisitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestVisitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='VISITOR')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestEmployee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestEmployee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestServiceEmployee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestServiceEmployee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestAdmin'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestAdmin'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestAdmin'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='ADMIN')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityManager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityManager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityManager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SECURITY_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityNightManager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityNightManager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityNightManager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SECURITY_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityGaurd'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityGaurd'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='SecurityGaurd'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SECURITY_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Laundry Employee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Laundry Employee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Laundry Employee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='LAUNDRY_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Laundry Manager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Laundry Manager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Laundry Manager'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='LAUNDRY_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Day Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Day Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Day Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SANITATION_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Night Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Night Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Night Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SANITATION_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Knight Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Knight Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Knight Janitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SANITATION_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Head of Maintenance'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Head of Maintenance'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Head of Maintenance'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='MAINTENANCE_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Maintenance Tech'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Maintenance Tech'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Maintenance Tech'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='MAINTENANCE_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Jen Barber'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Jen Barber'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Jen Barber'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='IT_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Maurice Moss'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Maurice Moss'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Maurice Moss'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='IT_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Roy Trenneman'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Roy Trenneman'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Roy Trenneman'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='IT_EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Spanish Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Spanish Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Spanish Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='TRANSLATOR')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Mandarin Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Mandarin Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Mandarin Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='TRANSLATOR')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Canadian Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Canadian Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Canadian Translator'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='TRANSLATOR')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Joy'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Joy'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Joy'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='NURSE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Ratched'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Ratched'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Ratched'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='NURSE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Cramer'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Cramer'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Cramer'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='NURSE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Hot Lips Houlihan'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Hot Lips Houlihan'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE')),"
              + "((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='Nurse Hot Lips Houlihan'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='NURSE'))");

      stmt.addBatch(
          "INSERT INTO ROLE_TO_PERMISSION (ROLE_ID, RESOURCE_ID) VALUES "
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='ADMIN'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='EDIT_MAP')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='ADMIN'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='EDIT_ANNOUNCEMENTS')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='VIEW_TICKET')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='REQUEST_TICKET')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='REQUEST_PARKING_PERMIT')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SECURITY_EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_SECURITY')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='LAUNDRY_EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_LAUNDRY')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='SANITATION_EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_SANITATION')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='IT_EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_AV')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='IT_EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_COMPUTER')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='TRANSLATOR'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_TRANSLATOR')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='NURSE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_MEDICINE_REQUEST')),"
              + "((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='MAINTENANCE_EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='RESPOND_TO_MAINTENANCE'))");

      stmt.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
