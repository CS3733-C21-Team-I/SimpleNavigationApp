package edu.wpi.ithorian.database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.ithorian.ticket.ServiceTicket;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class ServiceTicketDatabaseTests {
  @Test
  public void testServiceTicketDatabase() throws SQLException {
    //    NavDatabaseManager.init(true);
    //    ServiceTicketDatabaseManager.init(true);

    // populateServiceTicket();
    ServiceTicketDatabaseManager.getInstance().databaseRef.disconnect();
    NavDatabaseManager.getInstance().databaseRef.disconnect();
  }

  @Test
  public void testGetTicket() {
    // populateServiceTicket();

    ServiceTicket laundryTix =
        ServiceTicketDatabaseManager.getInstance().getTicketForRequestId(170683);
    assertEquals("LAUNDRY", laundryTix.getTicketType());
    assertEquals("ROOM304", laundryTix.getLocation());
    assertFalse(laundryTix.isCompleted());
    System.out.println("Test 1 passed");

    ServiceTicket mainteTix =
        ServiceTicketDatabaseManager.getInstance().getTicketForRequestId(100200);
    assertEquals("MAINTENANCE", mainteTix.getTicketType());
    assertEquals("ROOM106", mainteTix.getLocation());
    assertFalse(mainteTix.isCompleted());
    System.out.println("Test 2 passed");

    ServiceTicket foodTix =
        ServiceTicketDatabaseManager.getInstance().getTicketForRequestId(234567);
    assertEquals("FOOD", foodTix.getTicketType());
    assertEquals("ROOM205", foodTix.getLocation());
    assertTrue(foodTix.isCompleted());
    System.out.println("Test 3 passed");
  }

  @Test
  public void populateServiceTicket() {
    try {
      NavDatabaseManager.init(true);
      ServiceTicketDatabaseManager.init(true);

      Statement stmt =
          ServiceTicketDatabaseManager.getInstance().databaseRef.getConnection().createStatement();

      stmt.addBatch("INSERT INTO navMaps(MAP_ID) VALUES ('MAPG')\n");
      stmt.addBatch(
          "INSERT INTO navNodes(NODE_ID, X_COORD, Y_COORD, MAP_ID) VALUES ('ROOM304', 1, 2, 'MAPG')\n");
      stmt.addBatch(
          "INSERT INTO navNodes(NODE_ID, X_COORD, Y_COORD, MAP_ID) VALUES ('ROOM205', 3, 4, 'MAPG')\n");
      stmt.addBatch(
          "INSERT INTO navNodes(NODE_ID, X_COORD, Y_COORD, MAP_ID) VALUES ('ROOM106', 5, 6, 'MAPG')\n");

      stmt.addBatch(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description, completed)\n"
              + "    VALUES (170683, 123456, 'LAUNDRY', 'ROOM304', 'Patient dirty clothing', false)\n");
      stmt.addBatch(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description, completed)\n"
              + "VALUES (100200, 456789, 'MAINTENANCE', 'ROOM106', 'Broken TV', false)\n");
      stmt.addBatch(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description, completed)\n"
              + "VALUES (234567, 789123, 'FOOD', 'ROOM205', 'Dinner for patient', true)\n");
      stmt.executeLargeBatch();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
