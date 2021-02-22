package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceTicketDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";
  private static ServiceTicketDatabaseManager ourInstance;

  /** @param regen */
  public static void init(boolean regen) {
    ourInstance = new ServiceTicketDatabaseManager(regen);
    // throw new UnsupportedOperationException();
  }

  public static ServiceTicketDatabaseManager getInstance() {
    return ourInstance;
  }

  /** @param regen */
  private ServiceTicketDatabaseManager(boolean regen) {
    super(DB_URL, regen);
    // throw new UnsupportedOperationException();
  }

  /** @param id */
  public ServiceTicket getTicketForId(int id) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM serviceticket WHERE ticketID=" + String.valueOf(id));
      if (rs.next())
        return new ServiceTicket(
            id,
            rs.getInt("requestingUserID"),
            rs.getInt("assignedUserID"),
            ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
            rs.getString("location"),
            rs.getString("description"),
            rs.getBoolean("completed"));
      else return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<ServiceTicket> getTicketsForRequestId(int requestID) {
    List<ServiceTicket> results = new ArrayList<>();
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket WHERE REQUESTINGUSERID=" + String.valueOf(requestID));
      while (rs.next())
        results.add(
            new ServiceTicket(
                rs.getInt("ticketID"),
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed")));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    return results;
  }

  @Override
  void createTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      try { // Creating the ServiceTicket table
        stmt.execute(
            "create table serviceticket(\n"
                + "    ticketID         integer NOT NULL GENERATED ALWAYS AS IDENTITY,\n"
                + "    requestingUserID integer,\n"
                + "    assignedUserID   integer,\n"
                + "    ticketType       varchar(25),\n"
                + "    location         varchar(45) NOT NULL,\n"
                + "    description      varchar(50),\n"
                + "    completed        boolean,\n"
                + "    CONSTRAINT ticketID_PK PRIMARY KEY (ticketID),\n"
                + "    CONSTRAINT ticket_ck CHECK (serviceticket.ticketType in ('LAUNDRY', 'FOOD', 'SECURITY', 'MAINTENANCE')),\n"
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
  void dropTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      try {
        // Drop the ServiceTicket table.
        stmt.execute("DROP TABLE serviceticket");
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
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs = stmt.executeQuery("DELETE FROM serviceticket WHERE ticketID = '" + id + "'");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean updateTicket(int id) {
    // Update the complete-ness of a ticket
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM serviceticket WHERE ticketID = '" + id + "'");
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

  public void addTicket(ServiceTicket t) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      System.out.println(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description, completed)\n"
              + "VALUES ("
              + t.getRequestingUserID()
              + ","
              + t.getAssignedUserID()
              + ", '"
              + t.getTicketType().toString()
              + "', '"
              + t.getLocation()
              + "', '"
              + t.getDescription()
              + "', "
              + t.isCompleted()
              + ")");

      stmt.executeUpdate(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, ticketType, location, description, completed)\n"
              + "VALUES ("
              + t.getRequestingUserID()
              + ","
              + t.getAssignedUserID()
              + ", '"
              + t.getTicketType().toString()
              + "', '"
              + t.getLocation()
              + "', '"
              + t.getDescription()
              + "', "
              + t.isCompleted()
              + ")");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<ServiceTicket> getServiceTicketDB() {
    List<ServiceTicket> tix = new ArrayList<>();
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM serviceticket");
      while (rs.next()) {
        ServiceTicket cur =
            new ServiceTicket(
                rs.getInt("ticketID"),
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tix;
  }

  public static void populateExampleData() {
    ServiceTicket ticket1 =
        new ServiceTicket(
            11,
            UserDatabaseManager.getInstance().getUserForScreenname("TestEmployee").getUserId(),
            UserDatabaseManager.getInstance()
                .getUserForScreenname("TestMaintenanceEmployee")
                .getUserId(),
            ServiceTicket.TicketType.MAINTENANCE,
            "ICONF00103",
            "info",
            false);
    ServiceTicket ticket2 =
        new ServiceTicket(
            21,
            UserDatabaseManager.getInstance().getUserForScreenname("TestEmployee").getUserId(),
            UserDatabaseManager.getInstance()
                .getUserForScreenname("TestMaintenanceEmployee")
                .getUserId(),
            ServiceTicket.TicketType.LAUNDRY,
            "ICONF00104",
            "more info",
            false);

    ourInstance.addTicket(ticket1);
    ourInstance.addTicket(ticket2);
  }
}
