// package edu.wpi.ithorian.database;
//
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
//
// public class ServiceTicketDatabaseManager extends DatabaseManager {
//
//  private static final String DB_URL = "";
//  private static ServiceTicketDatabaseManager ourInstance;
//  private Connection connection = databaseRef.getConnection();
//
//  /** @param regen */
//  private ServiceTicketDatabaseManager(boolean regen) {
//    super(DB_URL, regen);
//    // TODO - implement ServiceTicket.ServiceTicketDatabaseManager
//    // throw new UnsupportedOperationException();
//  }
//
//  /** @param regen */
//  public static void init(boolean regen) {
//    ourInstance = new ServiceTicketDatabaseManager(regen);
//    // TODO - implement ServiceTicket.init
//    // throw new UnsupportedOperationException();
//  }
//
//  public ServiceTicketDatabaseManager getInstance() {
//    // TODO - implement ServiceTicket.getInstance
//    return ourInstance;
//  }
//
//  /** @param id */
//  public ServiceTicketDatabaseManager getTicketForId(int id) {
//    // TODO - implement ServiceTicket.getTicketForId
//    try {
//      Statement stmt = connection.createStatement();
//      ResultSet rs =
//          stmt.executeQuery("SELECT * FROM serviceticket WHERE ticketID=" + String.valueOf(id));
//      if (rs.next())
//        return new serviceTicket(
//            rs.getInt("requestingUserID"), rs.getInt("assignedUserID"),
//            rs.getString("ticketType"), rs.getObject("locationNode"),
//            rs.getString("description"), rs.getBoolean("completed"));
//      else return null;
//    } catch (SQLException e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//
//  @Override
//  protected void createTables() {
//    try {
//      Statement stmt = connection.createStatement();
//      try {
//        // Make locationNode type
//        stmt.execute(
//            "CREATE TYPE locationNode\n"
//                + "EXTERNAL NAME 'edu.wpi.ithorian.hospitalMap.LocationNode'\n"
//                + "LANGUAGE JAVA");
//      } catch (SQLException e) {
//        System.out.println("Error in generating locationNode type");
//      }
//
//      try { // Creating the ServiceTicket table
//        stmt.execute(
//            "create table serviceticket(\n"
//                + "ticketID         int,\n"
//                + "requestingUserID int,\n"
//                + "assignedUserID   int,\n"
//                + "ticketType       varchar(25),\n"
//                + "location         locationNode,\n"
//                + "description      varchar(50),\n"
//                + "completed        boolean,\n"
//                + "CONSTRAINT ticketID_PK PRIMARY KEY (ticketID),\n"
//                + "CONSTRAINT ticket_ck CHECK (ticketType in ('LAUNDRY', 'FOOD', 'SECURITY',
// 'MAINTENANCE')))");
//        System.out.println("ServiceTicket table created.");
//      } catch (SQLException e) {
//        System.out.println("Error in generating ServiceTicket Table");
//      }
//    } catch (SQLException throwables) {
//      throwables.printStackTrace();
//    }
//  }
//
//  @Override
//  protected void dropTables() {
//    try {
//      Statement stmt = connection.createStatement();
//      try {
//        // Drop the ServiceTicket table.
//        stmt.execute("DROP TABLE serviceticket ");
//        System.out.println("ServiceTicket table dropped.");
//      } catch (SQLException ex) {
//        // No need to report an error.
//        // The table simply did not exist.
//      }
//
//      try {
//        // Drop the LOCATIONNODE type.
//        stmt.execute("DROP TYPE LOCATIONNODE ");
//        System.out.println("LOCATIONNODE type dropped.");
//      } catch (SQLException ex) {
//        // No need to report an error.
//        // The table simply did not exist.
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
// }
