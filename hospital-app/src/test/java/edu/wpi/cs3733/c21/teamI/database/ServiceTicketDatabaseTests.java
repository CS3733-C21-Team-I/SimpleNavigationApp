package edu.wpi.cs3733.c21.teamI.database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class ServiceTicketDatabaseTests {
  //  @Test
  //  public void testServiceTicketDatabase() throws SQLException {
  //    //    NavDatabaseManager.init(true);
  //    //    ServiceTicketDatabaseManager.init(true);
  //
  //    // populateServiceTicket();
  //    ServiceTicketDatabaseManager.getInstance().databaseRef.disconnect();
  //    NavDatabaseManager.getInstance().databaseRef.disconnect();
  //  }

  @Test
  public void testGetTicket() {
    populateServiceTicket();

    ServiceTicket laundryTix =
        ServiceTicketDatabaseManager.getInstance().getTicketsForRequestId(170683).get(0);
    assertEquals("LAUNDRY", laundryTix.getTicketType().toString());
    assertEquals("ROOM304", laundryTix.getLocation());
    assertFalse(laundryTix.isCompleted());

    ServiceTicket mainteTix =
        ServiceTicketDatabaseManager.getInstance().getTicketsForRequestId(100200).get(0);
    assertEquals("MAINTENANCE", mainteTix.getTicketType().toString());
    assertEquals("ROOM106", mainteTix.getLocation());
    assertFalse(mainteTix.isCompleted());

    ServiceTicket foodTix =
        ServiceTicketDatabaseManager.getInstance().getTicketsForRequestId(234567).get(0);
    assertEquals("FOOD", foodTix.getTicketType().toString());
    assertEquals("ROOM205", foodTix.getLocation());
    assertTrue(foodTix.isCompleted());
  }

  public void populateServiceTicket() {
    try {
      DatabaseManager.initDatabaseManagers(true);
      DatabaseManager.regenTables();

      Statement stmt =
          ServiceTicketDatabaseManager.getInstance().databaseRef.getConnection().createStatement();

      stmt.addBatch("INSERT INTO navMaps(MAP_ID) VALUES ('MAPG')\n");
      stmt.addBatch(
          "INSERT INTO HOSPITAL_USERS(user_ID, screenName) VALUES (123456, 'ticketResponse')\n");
      stmt.addBatch("INSERT INTO HOSPITAL_USERS(user_ID, screenName) VALUES (170683, 'user1')\n");
      stmt.addBatch("INSERT INTO HOSPITAL_USERS(user_ID, screenName) VALUES (100200, 'user2')\n");
      stmt.addBatch("INSERT INTO HOSPITAL_USERS(user_ID, screenName) VALUES (234567, 'user3')\n");

      stmt.addBatch(
          "INSERT INTO navNodes(NODE_ID, X_COORD, Y_COORD, MAP_ID) VALUES ('ROOM304', 1, 2, 'MAPG')\n");
      stmt.addBatch(
          "INSERT INTO navNodes(NODE_ID, X_COORD, Y_COORD, MAP_ID) VALUES ('ROOM205', 3, 4, 'MAPG')\n");
      stmt.addBatch(
          "INSERT INTO navNodes(NODE_ID, X_COORD, Y_COORD, MAP_ID) VALUES ('ROOM106', 5, 6, 'MAPG')\n");

      stmt.addBatch(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description)\n"
              + "    VALUES (170683, 123456, 'LAUNDRY', 'ROOM304', 'Patient dirty clothing')\n");
      stmt.addBatch(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description)\n"
              + "VALUES (100200, 123456, 'MAINTENANCE', 'ROOM106', 'Broken TV')\n");
      stmt.addBatch(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description, completed)\n"
              + "VALUES (234567, 123456, 'FOOD', 'ROOM205', 'Dinner for patient', true)\n");
      stmt.executeLargeBatch();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
