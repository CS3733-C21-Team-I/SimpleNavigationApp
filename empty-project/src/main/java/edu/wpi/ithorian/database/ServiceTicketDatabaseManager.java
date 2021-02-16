package edu.wpi.ithorian.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceTicketDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "";
  private static ServiceTicketDatabaseManager ourInstance;
  private Connection connection = databaseRef.getConnection();

  /** @param regen */
  private ServiceTicketDatabaseManager(boolean regen) {
    super(DB_URL, regen);
    // throw new UnsupportedOperationException();
  }

  /** @param regen */
  public static void init(boolean regen) {
    ourInstance = new ServiceTicketDatabaseManager(regen);
    // throw new UnsupportedOperationException();
  }

  public ServiceTicketDatabaseManager getInstance() {
    return ourInstance;
  }

  /** @param id */
  public ServiceTicketDatabaseManager getTicketForId(int id) {
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM serviceticket WHERE ticketID=" + String.valueOf(id));
      if (rs.next())
        return new serviceTicket(
            rs.getInt("requestingUserID"), rs.getInt("assignedUserID"),
            rs.getString("ticketType"), rs.getObject("locationNode"),
            rs.getString("description"), rs.getBoolean("completed"));
      else return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  protected void createTables() {
    try {
      Statement stmt = connection.createStatement();
      try { // Creating the ServiceTicket table
        stmt.execute(
            "create table serviceticket(\n"
                + "    ticketID         int,\n"
                + "    requestingUserID int,\n"
                + "    assignedUserID   int,\n"
                + "    ticketType       varchar(25),\n"
                + "    location         varchar(45),\n"
                + "    description      varchar(50),\n"
                + "    completed        boolean,\n"
                + "    CONSTRAINT ticketID_PK PRIMARY KEY (ticketID),\n"
                + "    CONSTRAINT ticket_ck CHECK (serviceticket.ticketType in ('LAUNDRY', 'FOOD', 'SECURITY', 'MAINTENANCE'),\n"
                + "    CONSTRAINT location_FK FOREIGN KEY (location) REFERENCES navNodes(node_ID))");
        System.out.println("ServiceTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating ServiceTicket Table");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Override
  protected void dropTables() {
    try {
      Statement stmt = connection.createStatement();
      try {
        // Drop the ServiceTicket table.
        stmt.execute("DROP TABLE serviceticket ");
        System.out.println("ServiceTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void closeTicket(int id) {
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("DELETE FROM serviceticket WHERE ticketID = '" + id + "'");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean updateTicket(int id) {
    // Update the complete-ness of a ticket
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM serviceticket WHERE ticketID = '" + id + "'");
      if (!rs.next()) {
        return false;
      }
      stmt.execute("UPDATE serviceticket SET completed='" + true + "' WHERE ticketID='" + id + "'");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return true;
  }

  public void openTicket(int id) {
    getTicketForId(id);
  }
}
