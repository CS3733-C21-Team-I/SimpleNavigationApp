package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.ticket.*;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.*;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceTicketDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby://localhost:1527/navDB";
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
      String query = "SELECT * FROM serviceticket WHERE ticketID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      ServiceTicket ticket;
      if (rs.next()) {
        ticket =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"));
        ticket.setTicketID(rs.getInt("ticketID"));
        ticket.setAssignedUserID(getEmployeesForId(id));
        return ticket;
      } else return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Integer> getEmployeesForId(int id) {
    List<Integer> emps = new ArrayList<>();
    try {
      String query = "SELECT * FROM employee WHERE tixID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        emps.add(rs.getInt("employeeID"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return emps;
  }

  public List<ServiceTicket> getTicketsForRequestId(int requestID) {
    List<ServiceTicket> results = new ArrayList<>();
    try {
      String query = "SELECT * FROM serviceticket WHERE REQUESTINGUSERID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, requestID);
      ResultSet rs = stmt.executeQuery();
      int tixID = 0;
      ServiceTicket ticket;
      while (rs.next()) {
        ticket =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"));
        tixID = rs.getInt("TICKETID");
        ticket.setTicketID(tixID);
        ticket.setAssignedUserID(getEmployeesForId(tixID));
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
                + "    ticketType       varchar(25),\n"
                + "    location         varchar(45) NOT NULL,\n"
                + "    description      varchar(50),\n"
                + "    completed        boolean DEFAULT false,\n"
                + "    CONSTRAINT ticketID_PK PRIMARY KEY (ticketID),\n"
                + "    CONSTRAINT ticket_ck CHECK (serviceticket.ticketType in "
                + "('AUDIO_VISUAL', 'COMPUTER', 'LANGUAGE', 'PARKING', 'FLORAL', 'GIFT', 'INTERNAL_TRANSPORTATION',"
                + "'EXTERNAL_TRANSPORTATION', 'LAUNDRY', 'MEDICINE', 'RELIGIOUS', 'SECURITY', 'SANITATION', 'MAINTENANCE', 'COVID')),\n"
                + "    CONSTRAINT location_FK FOREIGN KEY (location) REFERENCES navNodes(node_ID),"
                + "  CONSTRAINT requestID_FK FOREIGN KEY (requestingUserID) REFERENCES HOSPITAL_USERS(user_ID))");
        // System.out.println("ServiceTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating ServiceTicket Table");
      }

      try { // Creating the employee table
        stmt.execute(
            "create table employee(\n"
                + "employeeID integer NOT NUll,\n"
                + "tixID integer NOT NULL,\n"
                + "PRIMARY KEY(employeeID, tixID),\n"
                + "FOREIGN KEY (employeeID) REFERENCES HOSPITAL_USERS(user_ID),\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("Employee table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating Employee Table");
      }

      try { // Creating the giftTicket table
        stmt.execute(
            "create table giftTicket(\n"
                + "giftID integer NOT NULL GENERATED ALWAYS AS IDENTITY,\n\n"
                + "patientName varchar(25),\n"
                + "deliveryDate varchar(50),\n"
                + "deliveryTime varchar(50),\n"
                + "giftType varchar(25),\n"
                + "location varchar(45) NOT NULL,\n"
                + "PRIMARY KEY(giftID),\n"
                + "FOREIGN KEY (location) REFERENCES navNodes(node_ID))");
        // System.out.println("giftTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating giftTicket Table");
      }

      try { // Creating the audioVisualTicket table
        stmt.execute(
            "create table audioVisualTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "patientName varchar(25),\n"
                + "mediaType varchar(25),\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("AudioVisualTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating AudioVisualTicket Table");
      }

      try { // Creating the computerTicket table
        stmt.execute(
            "create table computerTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "computerType varchar(25),\n"
                + "urgency boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("computerTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating computerTicket Table");
      }

      try { // Creating the covidTicket table
        stmt.execute(
            "create table covidTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "soreThroat boolean,\n"
                + "breathing boolean,\n"
                + "tasteSmellLoss boolean,\n"
                + "fever boolean,\n"
                + "congestion boolean,\n"
                + "cough boolean,\n"
                + "nausea boolean,\n"
                + "diarrhea boolean,\n"
                + "headache boolean,\n"
                + "noneSymptoms boolean,\n"
                + "contactCovidConfirmed boolean,\n"
                + "contactCovidSymptoms boolean,\n"
                + "noneContact boolean,\n"
                + "covidTest boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("covidTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating covidTicket Table");
      }

      try { // Creating the employeeParkingTicket table
        stmt.execute(
            "create table employeeParkingTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "licensePlate varchar(25),\n"
                + "contact integer,\n"
                + "startDate varchar(50),\n"
                + "endDate varchar(50),\n"
                + "disability boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("employeeParkingTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating employeeParkingTicket Table");
      }

      try { // Creating the externalTransportationTicket table
        stmt.execute(
            "create table externalTransportationTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "pickUpDate varchar(50),\n"
                + "pickUpTime varchar(50),\n"
                + "destination varchar(25),\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("externalTransportationTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating externalTransportationTicket Table");
      }

      try { // Creating the floralTicket table
        stmt.execute(
            "create table floralTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "patientName varchar(25),\n"
                + "deliveryDate varchar(50),\n"
                + "deliveryTime varchar(50),\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("floralTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating floralTicket Table");
      }

      try { // Creating the internalTransportationTicket table
        stmt.execute(
            "create table internalTransportationTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "pickUpDate varchar(50),\n"
                + "pickUpTime varchar(50),\n"
                + "destination varchar(25),\n"
                + "emergency boolean,\n"
                + "stretcher boolean,\n"
                + "wheelchair boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("internalTransportationTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating internalTransportationTicket Table");
      }

      try { // Creating the languageTicket table
        stmt.execute(
            "create table languageTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "language varchar(25),\n"
                + "meetingTime varchar(50),\n"
                + "legalDocs boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("languageTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating languageTicket Table");
      }

      try { // Creating the laundryTicket table
        stmt.execute(
            "create table laundryTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "pickUpDate varchar(50),\n"
                + "pickUpTime varchar(50),\n"
                + "dryClean boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("laundryTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating laundryTicket Table");
      }

      try { // Creating the medicineTicket table
        stmt.execute(
            "create table medicineTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "patientName varchar(25),\n"
                + "drugName varchar(25),\n"
                + "dose varchar(25),\n"
                + "date varchar(50),\n"
                + "time varchar(50),\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("medicineTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating medicineTicket Table");
      }

      try { // Creating the religiousTicket table
        stmt.execute(
            "create table religiousTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "patientName varchar(25),\n"
                + "religiousDenomination varchar(25),\n"
                + "religiousType varchar(25),\n"
                + "date varchar(50),\n"
                + "time varchar(50),\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("religiousTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating religiousTicket Table");
      }

      try { // Creating the securityTicket table
        stmt.execute(
            "create table securityTicket(\n"
                + "tixID integer NOT NULL,\n"
                + "securityType varchar(25),\n"
                + "emergency boolean,\n"
                + "FOREIGN KEY (tixID) REFERENCES SERVICETICKET(ticketID))");
        // System.out.println("securityTicket table created.");
      } catch (SQLException e) {
        System.out.println("Error in generating securityTicket Table");
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
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE audioVisualTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE computerTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE covidTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE employeeParkingTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE externalTransportationTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE floralTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE giftTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE internalTransportationTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE languageTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE laundryTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE medicineTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the religiousTicket table.
        stmt.execute("DROP TABLE religiousTicket");
        // System.out.println("religiousTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the securityTicket table.
        stmt.execute("DROP TABLE securityTicket");
        // System.out.println("securityTicket table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        // Drop the Employee table.
        stmt.execute("DROP TABLE employee");
        // System.out.println("Employee table dropped.");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

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
      String query = "DELETE FROM serviceticket WHERE ticketID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
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
          "UPDATE serviceticket SET completed='" + true + "' WHERE ticketID=" + cur.getTicketId());
      return cur;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void openTicket(int id) {
    getTicketForId(id);
  }

  public int addTicket(ServiceTicket t) {
    try {
      String query =
          "INSERT INTO serviceticket(requestingUserID, ticketType, location, description, completed)\n"
              + "VALUES (?, ?, ?, ?, ?)";
      PreparedStatement stmt =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      stmt.setInt(1, t.getRequestingUserID());
      stmt.setString(2, t.getTicketType().toString());
      stmt.setString(3, t.getLocation());
      stmt.setString(4, t.getDescription());
      stmt.setBoolean(5, t.isCompleted());
      stmt.execute();

      ResultSet rs = stmt.getGeneratedKeys();
      rs.next();
      int id = rs.getInt(1);

      // Add unique based on type
      switch (t.getTicketType()) {
        case AUDIO_VISUAL:
          ourInstance.addAudioVisual(id, (AudioVisualTicket) t);
          break;
        case COMPUTER:
          ourInstance.addComputer(id, (ComputerTicket) t);
          break;
        case COVID:
          ourInstance.addCovid(id, (CovidTicket) t);
          break;
        case LANGUAGE:
          ourInstance.addLanguage(id, (LanguageTicket) t);
          break;
        case PARKING:
          ourInstance.addEmployeeParking(id, (EmployeeParkingTicket) t);
          break;
        case INTERNAL_TRANSPORTATION:
          ourInstance.addInternalTransport(id, (InternalTransportationTicket) t);
          break;
        case LAUNDRY:
          ourInstance.addLaundry(id, (LaundryTicket) t);
          break;
        case MEDICINE:
          ourInstance.addMedicine(id, (MedicineTicket) t);
          break;
        case RELIGIOUS:
          ourInstance.addReligious(id, (ReligiousTicket) t);
          break;
        case SECURITY:
          ourInstance.addSecurity(id, (SecurityTicket) t);
          break;
        case EXTERNAL_TRANSPORTATION:
        case FLORAL:
        case SANITATION:
        case MAINTENANCE:
        default:
          break;
      }
      return id;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to insert ticket");
    }
  }

  public void addEmployee(int tixID, int employeeID) {
    try {
      String query = "INSERT INTO employee(employeeID, tixID) VALUES(?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, employeeID);
      stmt.setInt(2, tixID);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void removeEmployee(int tixID, int employeeID) {
    try {
      String query = "DELETE FROM employee WHERE employeeID = ? AND tixID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, employeeID);
      stmt.setInt(2, tixID);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void modifyEmployee(int tixID, int employeeID) {
    try {
      String query = "UPDATE employee SET employeeID = ? WHERE TIXID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, employeeID);
      stmt.setInt(2, tixID);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<ServiceTicket> getServiceTicketDB() {
    // Retrieve data from every possible joined table. Oh boi...
    List<ServiceTicket> tix = new ArrayList<>();
    ServiceTicket cur;
    int tixID = 0;
    // AV
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN AUDIOVISUALTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new AudioVisualTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("patientName"),
                rs.getString("mediaType"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Comp
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN COMPUTERTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new ComputerTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("computerType"),
                rs.getBoolean("urgency"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Covid
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN COVIDTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new CovidTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getBoolean("soreThroat"),
                rs.getBoolean("breathing"),
                rs.getBoolean("tasteSmellLoss"),
                rs.getBoolean("fever"),
                rs.getBoolean("congestion"),
                rs.getBoolean("cough"),
                rs.getBoolean("nausea"),
                rs.getBoolean("diarrhea"),
                rs.getBoolean("headache"),
                rs.getBoolean("noneSymptoms"),
                rs.getBoolean("contactCovidConfirmed"),
                rs.getBoolean("contactCovidSymptoms"),
                rs.getBoolean("noneContact"),
                rs.getBoolean("covidTest"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Park
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN EMPLOYEEPARKINGTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new EmployeeParkingTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("licensePlate"),
                rs.getInt("contact"),
                rs.getString("startDate"),
                rs.getString("endDate"),
                rs.getBoolean("disability"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // ExTransport
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN EXTERNALTRANSPORTATIONTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new ExternalTransportationTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("pickUpDate"),
                rs.getString("pickUpTime"),
                rs.getString("destination"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // InTransport
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN INTERNALTRANSPORTATIONTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new InternalTransportationTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("pickUpDate"),
                rs.getString("pickUpTime"),
                rs.getString("destination"),
                rs.getBoolean("emergency"),
                rs.getBoolean("stretcher"),
                rs.getBoolean("wheelchair"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Lang
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN LANGUAGETICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new LanguageTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("language"),
                rs.getString("meetingTime"),
                rs.getBoolean("legalDocs"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Laundry
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN LAUNDRYTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new LaundryTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("pickUpDate"),
                rs.getString("pickUpTime"),
                rs.getBoolean("dryClean"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Med
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN MEDICINETICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new MedicineTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("patientName"),
                rs.getString("drugName"),
                rs.getString("dose"),
                rs.getString("date"),
                rs.getString("time"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Religious
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN RELIGIOUSTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new ReligiousTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("patientName"),
                rs.getString("religiousDenomination"),
                rs.getString("religiousType"),
                rs.getString("date"),
                rs.getString("time"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Security
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM serviceticket st JOIN SECURITYTICKET UQ on st.TICKETID = UQ.TIXID");
      while (rs.next()) {
        cur =
            new SecurityTicket(
                rs.getInt("requestingUserID"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getString("securityType"),
                rs.getBoolean("emergency"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
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
          stmt.executeQuery("SELECT * FROM serviceticket WHERE requestingUserID = " + id);
      ServiceTicket cur;
      int tixID = 0;
      while (rs.next()) {
        cur =
            new ServiceTicket(
                rs.getInt("requestingUserID"),
                ServiceTicket.TicketType.valueOf(rs.getString("ticketType")),
                rs.getString("location"),
                rs.getString("description"),
                rs.getBoolean("completed"));
        tixID = rs.getInt("ticketID");
        cur.setTicketID(tixID);
        cur.setAssignedUserID(getEmployeesForId(tixID));
        tix.add(cur);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tix;
  }

  public static void populateExampleData() {
    /*
       int empRequestID =
    UserDatabaseManager.getInstance().getUserForScreenname("admin").getUserId();
           int empAssignID =


    UserDatabaseManager.getInstance().getUserForScreenname("TestServiceEmployee").getUserId();

       ServiceTicket ticket1 =
           new AudioVisualTicket(empRequestID, "ICONF00103", "info", false, "ZOLO", "HEADPHONE");
       ticket1.setTicketID(11);
           ServiceTicket ticket2 =
               new ServiceTicket(
                   empRequestID, ServiceTicket.TicketType.LAUNDRY, "ICONF00104", "more info",
    true);
           ticket2.setTicketID(21);

       ourInstance.addTicket(ticket1);
       ourInstance.addTicket(ticket2);


       try {
         Statement stmt = ourInstance.databaseRef.getConnection().createStatement();
         ResultSet rs =
             stmt.executeQuery(
                 "SELECT * FROM SERVICETICKET WHERE REQUESTINGUSERID=" +
    String.valueOf(empRequestID));
         int tixID = 0;
         if (rs.next()) {
           tixID = rs.getInt("ticketID");
           stmt.executeUpdate(

               "INSERT INTO employee(employeeID, tixID) VALUES(" + empAssignID + "," + tixID +
    ")");

         }
       } catch (SQLException e) {
         e.printStackTrace();
       }

    */
  }

  public void addEmployeeForTicket(int tixId, int assignID) {
    try {
      ourInstance.addEmployee(tixId, assignID);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addGift(GiftTicket gif) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.executeUpdate(
          "INSERT INTO GIFTTICKET(PATIENTNAME, DELIVERYDATE, DELIVERYTIME, GIFTTYPE, LOCATION)\n"
              + "VALUES('"
              + gif.getPatientName()
              + "', '"
              + gif.getDeliveryDate()
              + "', '"
              + gif.getDeliveryTime()
              + "', '"
              + gif.getGiftType()
              + "', '"
              + gif.getLocation()
              + "')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void removeGift(int id) {
    try {
      String query = "DELETE FROM GIFTTICKET WHERE GIFTID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, id);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Use to remove ANY ticket type that extends ServiceTicket
  public void removeUniqueTicket(int id, String ticketType) {
    try {
      String query = "DELETE FROM " + ticketType.toUpperCase() + "TICKET WHERE TIXID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, id);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addAudioVisual(int tixID, AudioVisualTicket av) {
    try {
      String query =
          "INSERT INTO AUDIOVISUALTICKET(tixID, PATIENTNAME, MEDIATYPE) VALUES (?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, av.getPatientName());
      stmt.setString(3, av.getMediaType());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addComputer(int tixID, ComputerTicket comp) {
    try {
      String query = "INSERT INTO COMPUTERTICKET(tixID, COMPUTERTYPE, URGENCY) VALUES (?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, comp.getComputerType());
      stmt.setBoolean(3, comp.isUrgency());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addCovid(int tixID, CovidTicket cov) {
    try {
      String query =
          "INSERT INTO COVIDTICKET(tixID, SORETHROAT, BREATHING, TASTESMELLLOSS, FEVER, CONGESTION, COUGH, NAUSEA, DIARRHEA, HEADACHE, NONESYMPTOMS, CONTACTCOVIDCONFIRMED, CONTACTCOVIDSYMPTOMS, NONECONTACT, COVIDTEST) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setBoolean(2, cov.isSoreThroat());
      stmt.setBoolean(3, cov.isBreathing());
      stmt.setBoolean(4, cov.isTasteSmellLoss());
      stmt.setBoolean(5, cov.isFever());
      stmt.setBoolean(6, cov.isCongestion());
      stmt.setBoolean(7, cov.isCough());
      stmt.setBoolean(8, cov.isNausea());
      stmt.setBoolean(9, cov.isDiarrhea());
      stmt.setBoolean(10, cov.isHeadache());
      stmt.setBoolean(11, cov.isNoneSymptoms());
      stmt.setBoolean(12, cov.isContactCovidConfirmed());
      stmt.setBoolean(13, cov.isContactCovidSymptoms());
      stmt.setBoolean(14, cov.isNoneContact());
      stmt.setBoolean(15, cov.isCovidTest());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addEmployeeParking(int tixID, EmployeeParkingTicket emp) {
    try {
      String query =
          "INSERT INTO EMPLOYEEPARKINGTICKET(tixID, LICENSEPLATE, CONTACT, STARTDATE, ENDDATE, DISABILITY) VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, emp.getLicensePlate());
      stmt.setInt(3, emp.getContact());
      stmt.setString(4, emp.getStartDate());
      stmt.setString(5, emp.getEndDate());
      stmt.setBoolean(6, emp.isDisability());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addInternalTransport(int tixID, InternalTransportationTicket inT) {
    try {
      String query =
          "INSERT INTO INTERNALTRANSPORTATIONTICKET(tixID, PICKUPDATE, PICKUPTIME, DESTINATION, EMERGENCY, STRETCHER, WHEELCHAIR) VALUES (?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, inT.getPickUpDate());
      stmt.setString(3, inT.getPickUpTime());
      stmt.setString(4, inT.getDestination());
      stmt.setBoolean(5, inT.isEmergency());
      stmt.setBoolean(6, inT.isStretcher());
      stmt.setBoolean(7, inT.isWheelchair());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addLanguage(int tixID, LanguageTicket lang) {
    try {
      String query =
          "INSERT INTO LANGUAGETICKET(tixID, LANGUAGE, MEETINGTIME, LEGALDOCS) VALUES (?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, lang.getLanguage());
      stmt.setString(3, lang.getMeetingTime());
      stmt.setBoolean(4, lang.isLegalDocs());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addLaundry(int tixID, LaundryTicket lau) {
    try {
      String query =
          "INSERT INTO LAUNDRYTICKET(tixID, PICKUPDATE, PICKUPTIME, DRYCLEAN) VALUES (?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, lau.getPickUpDate());
      stmt.setString(3, lau.getPickUpTime());
      stmt.setBoolean(4, lau.isDryClean());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addMedicine(int tixID, MedicineTicket med) {
    try {
      String query =
          "INSERT INTO MEDICINETICKET(tixID, PATIENTNAME, DRUGNAME, DOSE, DATE, TIME) VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, med.getPatientName());
      stmt.setString(3, med.getDrugName());
      stmt.setString(4, med.getDose());
      stmt.setString(5, med.getDate());
      stmt.setString(6, med.getTime());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addReligious(int tixID, ReligiousTicket reg) {
    try {
      String query =
          "INSERT INTO RELIGIOUSTICKET(tixID, PATIENTNAME, RELIGIOUSDENOMINATION, RELIGIOUSTYPE, DATE, TIME) VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, reg.getPatientName());
      stmt.setString(3, reg.getReligiousDenomination());
      stmt.setString(4, reg.getReligiousType());
      stmt.setString(5, reg.getDate());
      stmt.setString(6, reg.getTime());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addSecurity(int tixID, SecurityTicket sec) {
    try {
      String query = "INSERT INTO SECURITYTICKET(tixID, SECURITYTYPE, EMERGENCY) VALUES (?, ?, ?)";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, tixID);
      stmt.setString(2, sec.getSecurityType());
      stmt.setBoolean(3, sec.isEmergency());
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
