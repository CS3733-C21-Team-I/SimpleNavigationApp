package edu.wpi.cs3733.c21.teamI.database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class UserDatabaseTests {

  @Test
  public void testUserDatabaseStructure() {
    UserDatabaseManager.init(true);

    try {
      populateTestData();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    UserDatabaseManager.getInstance().databaseRef.disconnect();
  }

  @Test
  public void testGetUser() {
    UserDatabaseManager.init(true);

    try {
      populateTestData();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    User admin = UserDatabaseManager.getInstance().getUserForScreenname("TestAdmin");

    assertTrue(admin.userRoles.contains(User.Role.BASE));
    assertTrue(admin.userRoles.contains(User.Role.EMPLOYEE));
    assertTrue(admin.userRoles.contains(User.Role.ADMIN));

    assertTrue(admin.hasPermission(User.Permission.EDIT_MAP));
    assertTrue(admin.hasPermission(User.Permission.VIEW_TICKET));
    assertTrue(admin.hasPermission(User.Permission.REQUEST_TICKET));
    assertTrue(admin.hasPermission(User.Permission.EDIT_ANNOUNCEMENTS));

    User employee = UserDatabaseManager.getInstance().getUserForScreenname("TestEmployee");

    assertTrue(employee.userRoles.contains(User.Role.BASE));
    assertTrue(employee.userRoles.contains(User.Role.EMPLOYEE));
    assertFalse(employee.userRoles.contains(User.Role.ADMIN));

    assertFalse(employee.hasPermission(User.Permission.EDIT_MAP));
    assertTrue(employee.hasPermission(User.Permission.VIEW_TICKET));
    assertTrue(employee.hasPermission(User.Permission.REQUEST_TICKET));
    assertFalse(employee.hasPermission(User.Permission.EDIT_ANNOUNCEMENTS));
  }

  void populateTestData() throws SQLException {
    Statement stmt =
        UserDatabaseManager.getInstance().databaseRef.getConnection().createStatement();
    stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestVisitor')");
    stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestEmployee')");
    stmt.addBatch("INSERT INTO HOSPITAL_USERS (SCREENNAME) VALUES ('TestAdmin')");

    stmt.addBatch(
        "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES ('BASE', 'TODO')");
    stmt.addBatch(
        "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES ('ADMIN', 'TODO')");
    stmt.addBatch(
        "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES ('VISITOR', 'TODO')");
    stmt.addBatch(
        "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES ('EMPLOYEE', 'TODO')");
    stmt.addBatch(
        "INSERT INTO HOSPITAL_ROLES (ROLE_NAME, ROLE_DESCRIPTION) VALUES ('PATIENT', 'TODO')");

    stmt.addBatch(
        "INSERT INTO RESOURCE_PERMISSIONS (RESOURCE_NAME, RESOURCE_DESCRIPTION) VALUES ('EDIT_MAP', 'Place and Edit nodes on the hospital map')");
    stmt.addBatch(
        "INSERT INTO RESOURCE_PERMISSIONS (RESOURCE_NAME, RESOURCE_DESCRIPTION) VALUES ('VIEW_TICKET', 'TODO')");
    stmt.addBatch(
        "INSERT INTO RESOURCE_PERMISSIONS (RESOURCE_NAME, RESOURCE_DESCRIPTION) VALUES ('REQUEST_TICKET', 'TODO')");
    stmt.addBatch(
        "INSERT INTO RESOURCE_PERMISSIONS (RESOURCE_NAME, RESOURCE_DESCRIPTION) VALUES ('EDIT_ANNOUNCEMENTS', 'TODO')");

    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestVisitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE'))");
    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestVisitor'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='VISITOR'))");

    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestEmployee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE'))");
    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestEmployee'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'))");

    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestAdmin'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='BASE'))");
    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE  (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestAdmin'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'))");
    stmt.addBatch(
        "INSERT INTO USER_TO_ROLE  (USER_ID, ROLE_ID) VALUES ((SELECT USER_ID FROM HOSPITAL_USERS WHERE SCREENNAME='TestAdmin'), (SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='ADMIN'))");

    stmt.addBatch(
        "INSERT INTO ROLE_TO_PERMISSION (ROLE_ID, RESOURCE_ID) VALUES ((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='ADMIN'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='EDIT_MAP'))");
    stmt.addBatch(
        "INSERT INTO ROLE_TO_PERMISSION (ROLE_ID, RESOURCE_ID) VALUES ((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='ADMIN'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='EDIT_ANNOUNCEMENTS'))");

    stmt.addBatch(
        "INSERT INTO ROLE_TO_PERMISSION (ROLE_ID, RESOURCE_ID) VALUES ((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='VIEW_TICKET'))");
    stmt.addBatch(
        "INSERT INTO ROLE_TO_PERMISSION (ROLE_ID, RESOURCE_ID) VALUES ((SELECT ROLE_ID FROM HOSPITAL_ROLES WHERE ROLE_NAME='EMPLOYEE'),(SELECT RESOURCE_ID FROM RESOURCE_PERMISSIONS WHERE RESOURCE_NAME='REQUEST_TICKET'))");

    stmt.executeBatch();
  }
}
