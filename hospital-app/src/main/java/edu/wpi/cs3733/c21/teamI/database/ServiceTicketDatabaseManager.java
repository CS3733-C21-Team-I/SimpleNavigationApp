package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceTicketDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";
  private static ServiceTicketDatabaseManager ourInstance;
  private List<ServiceTicket> serviceTixLs;

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
      if (rs.next()) {
        ServiceTicket ticket =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                rs.getString("requestType"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("emergency"),
                rs.getBoolean("completed"));
        ticket.setTicketID(rs.getInt("ID"));
        return ticket;
      } else return null;
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
      while (rs.next()) {
        ServiceTicket ticket =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                rs.getString("requestType"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("emergency"),
                rs.getBoolean("completed"));
        ticket.setTicketID(rs.getInt("TICKETID"));
        results.add(ticket);
      }

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
                + "    requestingUserID integer NOT NULL,\n"
                + "    assignedUserID   integer NOT NULL,\n"
                + "    requestType      varchar(25),\n"
                + "    ticketType       varchar(25),\n"
                + "    location         varchar(45) NOT NULL,\n"
                + "    description      varchar(50),\n"
                + "    emergency        boolean,\n"
                + "    completed        boolean DEFAULT false,\n"
                + "    CONSTRAINT ticketID_PK PRIMARY KEY (ticketID),\n"
                + "    CONSTRAINT ticket_ck CHECK (serviceticket.ticketType in ('LAUNDRY', 'FOOD', 'SECURITY', 'SANITATION', 'MAINTENANCE')),\n"
                + "    CONSTRAINT location_FK FOREIGN KEY (location) REFERENCES navNodes(node_ID),"
                + "  CONSTRAINT requestID_FK FOREIGN KEY (requestingUserID) REFERENCES HOSPITAL_USERS(user_ID),"
                + "  CONSTRAINT assignedID_FK FOREIGN KEY (assignedUserID) REFERENCES  HOSPITAL_USERS(user_ID))");
        // System.out.println("ServiceTicket table created.");
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
        // System.out.println("ServiceTicket table dropped.");
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
      ResultSet rs =
          stmt.executeQuery("DELETE FROM serviceticket WHERE ticketID = " + String.valueOf(id));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ServiceTicket updateTicket(int id) {
    // Update the complete-ness of a ticket
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ServiceTicket cur = getTicketForId(id);
      cur.setCompleted(true);
      stmt.execute(
          "UPDATE serviceticket SET completed='"
              + true
              + "' WHERE ticketID="
              + String.valueOf(cur.getTicketId()));
      return cur;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void openTicket(int id) {
    getTicketForId(id);
  }

  public void addTicket(ServiceTicket t) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();

      stmt.executeUpdate(
          "INSERT INTO serviceticket(requestingUserID, assignedUserID, requestType, ticketType, location, description, emergency, completed)\n"
              + "VALUES ("
              + t.getRequestingUserID()
              + ", "
              + t.getAssignedUserID()
              + ", '"
              + t.getRequestType()
              + "', '"
              + t.getTicketType().toString()
              + "', '"
              + t.getLocation()
              + "', '"
              + t.getDescription()
              + "', "
              + t.isEmergency()
              + ", "
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
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                rs.getString("requestType"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("emergency"),
                rs.getBoolean("completed"));
        cur.setTicketID(rs.getInt("ticketID"));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tix;
  }

  public List<ServiceTicket> getRequestServiceTicket(User user) {
    List<ServiceTicket> tix = new ArrayList<>();
    int id = user.getUserId();
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket WHERE requestingUserID = " + String.valueOf(id));
      while (rs.next()) {
        ServiceTicket cur =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                rs.getString("requestType"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("emergency"),
                rs.getBoolean("completed"));
        cur.setTicketID(rs.getInt("ticketID"));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tix;
  }

  public List<ServiceTicket> getAssignServiceTicket(User user) {
    List<ServiceTicket> tix = new ArrayList<>();
    int id = user.getUserId();
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket WHERE requestingUserID = " + String.valueOf(id));
      while (rs.next()) {
        ServiceTicket cur =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                rs.getInt("assignedUserID"),
                rs.getString("requestType"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("emergency"),
                rs.getBoolean("completed"));
        cur.setTicketID(rs.getInt("ticketID"));
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
            UserDatabaseManager.getInstance().getUserForScreenname("TestEmployee").getUserId(),
            UserDatabaseManager.getInstance()
                .getUserForScreenname("TestServiceEmployee")
                .getUserId(),
            "TESTYPE1",
            ServiceTicket.TicketType.MAINTENANCE,
            "ICONF00103",
            "info",
            true,
            false);
    ticket1.setTicketID(11);
    ServiceTicket ticket2 =
        new ServiceTicket(
            UserDatabaseManager.getInstance().getUserForScreenname("TestEmployee").getUserId(),
            UserDatabaseManager.getInstance()
                .getUserForScreenname("TestServiceEmployee")
                .getUserId(),
            "TESTYPE2",
            ServiceTicket.TicketType.LAUNDRY,
            "ICONF00104",
            "more info",
            false,
            true);
    ticket2.setTicketID(21);

    ourInstance.addTicket(ticket1);
    ourInstance.addTicket(ticket2);
  }
}
