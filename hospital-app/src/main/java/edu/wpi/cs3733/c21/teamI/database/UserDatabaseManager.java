package edu.wpi.cs3733.c21.teamI.database;

import static edu.wpi.cs3733.c21.teamI.user.Employee.Gender.*;
import static edu.wpi.cs3733.c21.teamI.user.User.Permission.*;
import static edu.wpi.cs3733.c21.teamI.user.User.Role.*;

import edu.wpi.cs3733.c21.teamI.user.Employee;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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

  public User getUserWithPassword(String screenName, String password) {
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
      byte[] salt = rs.getBytes("SALT");
      byte[] hashed = rs.getBytes("hashed_password");

      if (!Password.isExpectedPassword(password.toCharArray(), salt, hashed)) {
        return null;
      }

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
              + " user_id    integer NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + " screenName varchar(30) NOT NULL ,"
              + "hashed_password blob(32),"
              + "salt blob(32),"
              + "PRIMARY KEY (user_ID)"
              + ")");
    } catch (SQLException e) {
      System.out.println("Error generating User table");
      e.printStackTrace();
    }

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "CREATE TABLE HOSPITAL_EMPLOYEES"
              + "("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "user_ID    integer NOT NULL, "
              + "first_name varchar(30) NOT NULL, "
              + "last_name varchar(30) NOT NULL,"
              + "gender varchar(8),"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY(user_ID) REFERENCES HOSPITAL_USERS(user_id)"
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
      stmt.execute("DROP TABLE HOSPITAL_EMPLOYEES");
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

    for (User.Permission permission : User.Permission.values()) {
      ourInstance.createNewPermission(permission, "TODO");
    }

    ourInstance.createNewRole(BASE, "TODO");
    ourInstance.createNewRole(ADMIN, "TODO", EDIT_MAP, EDIT_ANNOUNCEMENTS);
    ourInstance.createNewRole(
        EMPLOYEE, "TODO", REQUEST_PARKING_PERMIT, REQUEST_TICKET, VIEW_TICKET);
    ourInstance.createNewRole(PATIENT, "TODO", REQUEST_TICKET);
    ourInstance.createNewRole(SECURITY_EMPLOYEE, "TODO", RESPOND_TO_SECURITY);
    ourInstance.createNewRole(LAUNDRY_EMPLOYEE, "TODO", RESPOND_TO_LAUNDRY);
    ourInstance.createNewRole(SANITATION_EMPLOYEE, "TODO", RESPOND_TO_SANITATION);
    ourInstance.createNewRole(MAINTENANCE_EMPLOYEE, "TODO", RESPOND_TO_MAINTENANCE);
    ourInstance.createNewRole(IT_EMPLOYEE, "TODO", RESPOND_TO_COMPUTER, RESPOND_TO_AV);
    ourInstance.createNewRole(TRANSLATOR, "TODO", RESPOND_TO_TRANSLATOR);
    ourInstance.createNewRole(NURSE, "TODO", RESPOND_TO_MEDICINE_REQUEST);

    //      Statement stmt = ourInstance.databaseRef.getConnection().createStatement();
    //      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestVisitor')");
    //      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestEmployee')");
    //      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES
    // ('TestServiceEmployee')");
    //      stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestAdmin')");

    ourInstance.createNewUser("admin", "admin", ADMIN, EMPLOYEE);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
    //              + "('SecurityManager'),"
    //              + "('SecurityNightManager'),"
    //              + "('SecurityGaurd')");
    ourInstance.createNewEmployee("Security Manager", "", "David", "Dun", MALE, SECURITY_EMPLOYEE);
    ourInstance.createNewEmployee(
        "Security Night Manager", "", "Larry", "Daley", MALE, SECURITY_EMPLOYEE);
    ourInstance.createNewEmployee("Security Gaurd", "", "Ralph", "Gaurd", MALE, SECURITY_EMPLOYEE);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
    //              + "('Laundry Employee'),"
    //              + "('Laundry Manager')");
    ourInstance.createNewEmployee("Laundry Employee", "", "John", "Smith", MALE, LAUNDRY_EMPLOYEE);
    ourInstance.createNewEmployee("Laundry Manager", "", "Jane", "Smith", FEMALE, LAUNDRY_EMPLOYEE);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
    //              + "('Day Janitor'),"
    //              + "('Night Janitor'),"
    //              + "('Knight Janitor')");
    ourInstance.createNewEmployee(
        "Day Janitor", "", "Groundskeeper", "Willie", MALE, SANITATION_EMPLOYEE);
    ourInstance.createNewEmployee(
        "Night Janitor", "", "Will", "Hunting", MALE, SANITATION_EMPLOYEE);
    ourInstance.createNewEmployee("Knight Janitor", "", "Scruffy", "", MALE, SANITATION_EMPLOYEE);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
    //              + "('Head of Maintenance'),"
    //              + "('Maintenance Tech')");
    ourInstance.createNewEmployee(
        "Head of Maintenance", "", "Naomi", "Nagata", FEMALE, MAINTENANCE_EMPLOYEE);
    ourInstance.createNewEmployee(
        "Mainenance Tech", "", "Amos", "Burton", MALE, MAINTENANCE_EMPLOYEE);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
    //              + "('Jen Barber'),"
    //              + "('Roy Trenneman'),"
    //              + "('Maurice Moss')");
    ourInstance.createNewEmployee("Head of IT", "", "Jen", "Barber", FEMALE, IT_EMPLOYEE);
    ourInstance.createNewEmployee("IT Desk Tech", "", "Roy", "Tenneman", MALE, IT_EMPLOYEE);
    ourInstance.createNewEmployee("IT Tech", "", "Maurice", "Moss", MALE, IT_EMPLOYEE);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES "
    //              + "('Spanish Translator'),"
    //              + "('Canadian Translator'),"
    //              + "('Mandarin Translator')");
    ourInstance.createNewEmployee("Spanish Translator", "", "Mr", "Melas", MALE, TRANSLATOR);
    ourInstance.createNewEmployee(
        "Canadian Translator", "", "Missandai", "of Narth", FEMALE, TRANSLATOR);
    ourInstance.createNewEmployee("Mandarin Translator", "", "Babel", "Fish", OTHER, TRANSLATOR);

    //      stmt.addBatch(
    //          "INSERT INTO HOSPITAL_USERS(SCREENNAME) VALUES "
    //              + "('Nurse Joy'),"
    //              + "('Nurse Ratched'),"
    //              + "('Nurse Cramer'),"
    //              + "('Nurse Hot Lips Houlihan')");
    ourInstance.createNewEmployee("Nurse Joy", "", "", "Joy", FEMALE, NURSE);
    ourInstance.createNewEmployee("Nurse Ratched", "", "", "Ratched", FEMALE, NURSE);
    ourInstance.createNewEmployee("Nurse Petrelli", "", "Peter", "Petrelli", MALE, NURSE);
    ourInstance.createNewEmployee(
        "Nurse Hot Lips Houlihan", "", "Margret", "Houlihan", FEMALE, NURSE);

    /*
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
    */

    /*
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

     */

    /*
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

    */

    /*
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
    */
    //      stmt.executeBatch();

  }

  public List<Employee> getAllEmployees() {
    System.out.println("All employees got");
    try {
      List<Employee> out = new ArrayList<>();

      System.out.println("Employee pre query");
      String query =
          "SELECT * FROM HOSPITAL_EMPLOYEES JOIN HOSPITAL_USERS HU on HU.USER_ID = HOSPITAL_EMPLOYEES.USER_ID";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        System.out.println("Employee adding");

        Set<User.Role> rolesSet = new HashSet<>();

        try {
          Statement m_statement = databaseRef.getConnection().createStatement();
          ResultSet m_rs =
              m_statement.executeQuery(
                  "SELECT HR.ROLE_NAME FROM USER_TO_ROLE INNER JOIN HOSPITAL_ROLES HR on HR.ROLE_ID = USER_TO_ROLE.ROLE_ID WHERE USER_ID="
                      + rs.getInt("USER_ID"));

          while (m_rs.next()) {
            rolesSet.add(User.Role.valueOf(m_rs.getString("ROLE_NAME")));
          }

          if (!rolesSet.contains(User.Role.BASE)) {
            // TODO Error Logging
            throw new IllegalStateException(
                "Requested user: " + rs.getInt("USER_ID") + " does not contain role BASE");
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }

        Set<User.Permission> permissionSet = new HashSet<>();

        try {

          for (User.Role role : rolesSet) {
            Statement m_statement = databaseRef.getConnection().createStatement();
            ResultSet m_rs =
                m_statement.executeQuery(
                    "SELECT RP.RESOURCE_NAME FROM ROLE_TO_PERMISSION INNER JOIN RESOURCE_PERMISSIONS RP on ROLE_TO_PERMISSION.RESOURCE_ID = RP.RESOURCE_ID WHERE ROLE_ID=(SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='"
                        + role.toString()
                        + "')");

            while (m_rs.next()) {
              permissionSet.add(User.Permission.valueOf(m_rs.getString("RESOURCE_NAME")));
            }
          }
        } catch (SQLException e) {
          // TODO ERROR Logging
          e.printStackTrace();
        }

        out.add(
            new Employee(
                rs.getInt("USER_ID"),
                rs.getString("SCREENNAME"),
                rolesSet,
                permissionSet,
                rs.getString("first_name"),
                rs.getString("last_name"),
                Employee.Gender.valueOf(rs.getString("gender"))));
      }

      return out;
    } catch (SQLException e) {
      System.out.println("Employee get exception");
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public int createNewPermission(User.Permission permission, String description) {
    int insertedPermission = -1;
    try {
      String query =
          "INSERT INTO RESOURCE_PERMISSIONS(RESOURCE_NAME, RESOURCE_DESCRIPTION) VALUES"
              + "('"
              + permission.toString()
              + "', '"
              + description
              + "')";

      PreparedStatement statement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      statement.execute();

      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      insertedPermission = rs.getInt(1);
    } catch (SQLException e) {
      printSQLException(e);
    }

    if (insertedPermission == -1)
      throw new IllegalArgumentException(
          "Failed to insert permission: " + permission + " into table");

    return insertedPermission;
  }

  public int createNewRole(User.Role role, String description, User.Permission... permissions) {
    int insertedRole = -1;
    try {
      String query =
          "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES "
              + "('"
              + role.toString()
              + "', '"
              + description
              + "')";

      PreparedStatement statement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      statement.execute();

      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      insertedRole = rs.getInt(1);
    } catch (SQLException e) {
      printSQLException(e);
    }

    if (insertedRole == -1)
      throw new IllegalArgumentException("Failed to insert role: " + role + " into table");

    try {
      for (User.Permission permission : permissions) {
        String permQuery =
            "SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='" + permission + "'";
        PreparedStatement permStatement = databaseRef.getConnection().prepareStatement(permQuery);
        ResultSet rs = permStatement.executeQuery();

        int permId = -1;
        if (rs.next()) permId = rs.getInt("RESOURCE_ID");

        if (permId == -1)
          throw new IllegalArgumentException(
              "Permission: " + permission.toString() + " not found in DB");

        String roleToPermQuery =
            "INSERT INTO ROLE_TO_PERMISSION (ROLE_ID, RESOURCE_ID) VALUES "
                + "("
                + insertedRole
                + ", "
                + permId
                + ")";
        PreparedStatement roleToPermQueryStatement =
            databaseRef.getConnection().prepareStatement(roleToPermQuery);
        roleToPermQueryStatement.execute();
      }
    } catch (SQLException e) {
      printSQLException(e);
    }

    return insertedRole;
  }

  public int createNewUser(String username, String password, User.Role... m_roles) {
    List<User.Role> roles = new ArrayList<>(Arrays.asList(m_roles));
    roles.add(BASE);

    int insertedUser = -1;
    if (password.isEmpty()) {
      try {
        String query = "INSERT INTO HOSPITAL_USERS (screenname) VALUES ";
        query += "('" + username + "')";
        PreparedStatement statement =
            databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        insertedUser = rs.getInt(1);
      } catch (SQLException e) {
        printSQLException(e);
      }
    } else {

      try {
        byte[] salt = Password.getNextSalt();
        byte[] hashedPassword = Password.hash(password.toCharArray(), salt);

        System.out.println(salt);

        String query = "INSERT INTO HOSPITAL_USERS (screenname, hashed_password, salt) VALUES ";
        query += "('" + username + "', ?, ?)";
        PreparedStatement statement =
            databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setBytes(1, hashedPassword);
        statement.setBytes(2, salt);
        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        insertedUser = rs.getInt(1);

      } catch (SQLException e) {
        printSQLException(e);
      }
    }

    if (insertedUser == -1)
      throw new IllegalArgumentException("Illegal returnedkey for insering new user");

    try {
      for (User.Role role : roles) {
        String query =
            "INSERT INTO USER_TO_ROLE(USER_ID, ROLE_ID) VALUES ("
                + insertedUser
                + ", (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='"
                + role.toString()
                + "'))";

        PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
        statement.execute();
      }
    } catch (SQLException e) {
      printSQLException(e);
    }

    return insertedUser;
  }

  public int createNewEmployee(
      String username,
      String password,
      String firstName,
      String lastName,
      Employee.Gender gender,
      User.Role... m_roles) {

    List<User.Role> roles = new ArrayList<>(Arrays.asList(m_roles));
    roles.add(EMPLOYEE);

    User.Role[] rArr = new User.Role[roles.size()];
    int insertedUser = createNewUser(username, password, roles.toArray(rArr));
    if (insertedUser == -1)
      throw new IllegalStateException("Failed to insert user to DB when creating employee");

    int insertedEmployee = -1;

    try {
      String query =
          "INSERT INTO HOSPITAL_EMPLOYEES (USER_ID, FIRST_NAME, LAST_NAME, GENDER) VALUES "
              + "("
              + insertedUser
              + ", '"
              + firstName
              + "', '"
              + lastName
              + "', '"
              + gender.toString()
              + "')";
      PreparedStatement statement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      statement.execute();

      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      insertedEmployee = rs.getInt(1);
    } catch (SQLException e) {
      printSQLException(e);
    }

    if (insertedEmployee == -1) throw new IllegalStateException("Failed to insert employee");

    return insertedEmployee;
  }
}
