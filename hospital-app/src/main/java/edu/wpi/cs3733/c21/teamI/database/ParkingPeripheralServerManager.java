package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.parking.Block;
import edu.wpi.cs3733.c21.teamI.parking.Floor;
import edu.wpi.cs3733.c21.teamI.parking.Lot;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingCustomer;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingReservation;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingSlip;
import edu.wpi.cs3733.c21.teamI.parking.reservations.StaffPermit;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ParkingPeripheralServerManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby://localhost:1527/peripheralDB";

  private static ParkingPeripheralServerManager ourInstance;

  public static void init(boolean regen) {
    ourInstance = new ParkingPeripheralServerManager(regen);
  }

  public static ParkingPeripheralServerManager getInstance() {
    return ourInstance;
  }

  private ParkingPeripheralServerManager(boolean regen) {
    super(DB_URL, regen);
  }

  public Map<String, Lot> loadLots() {

    try {
      Map<String, Lot> lots = new HashMap<>();
      Statement lotStatement = databaseRef.getConnection().createStatement();
      ResultSet lotResults = lotStatement.executeQuery("SELECT * FROM PARKING_LOTS");

      while (lotResults.next()) {
        Lot lot =
            new Lot(
                lotResults.getInt("ID"),
                lotResults.getString("NAME"),
                lotResults.getBoolean("IS_REENTRY_ALLOWED"),
                lotResults.getBoolean("IS_VALLET_AVAILABLE"));
        System.out.println(lot.getName());

        Statement blockStatement = databaseRef.getConnection().createStatement();
        ResultSet blockResults =
            blockStatement.executeQuery(
                "SELECT * FROM PARKING_BLOCKS WHERE PARKING_LOT_ID=" + lotResults.getInt("ID"));

        while (blockResults.next()) {
          Block block = new Block(blockResults.getInt("ID"), blockResults.getString("BLOCK_CODE"));

          Statement floorStatement = databaseRef.getConnection().createStatement();
          ResultSet floorResults =
              floorStatement.executeQuery(
                  "SELECT * FROM PARKING_FLOORS WHERE BLOCK_ID=" + blockResults.getInt("ID"));

          while (floorResults.next()) {
            Map<String, Integer> slotCodes = new HashMap<>();
            Statement slotStatement = databaseRef.getConnection().createStatement();
            ResultSet slotResults =
                slotStatement.executeQuery(
                    "SELECT * FROM PARKING_SLOTS WHERE FLOOR_ID=" + floorResults.getInt("ID"));

            while (slotResults.next()) {
              slotCodes.put(
                  slotResults.getString("CODE") + slotResults.getInt("SLOT_NUMBER"),
                  slotResults.getInt("ID"));
            }

            Floor floor =
                new Floor(
                    floorResults.getInt("ID"),
                    slotCodes,
                    floorResults.getInt("FLOOR_NUMBER"),
                    floorResults.getBoolean("IS_COVERED"),
                    floorResults.getBoolean("IS_ACCESSIBLE"),
                    floorResults.getBoolean("IS_RESERVED_STAFF"));
            block.addFloor(floor);
          }
          lot.addBlock(block);
        }
        lots.put(lot.getName(), lot);
      }

      return lots;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public int getUnocupiedSlotsForLot(Lot lot) {
    int out = 0;
    for (Block b : lot.getBlocks()) {
      int i = getUnocupiedSlotsForBlock(b);
      if (i == -1) return -1;
      out += i;
    }
    return out;
  }

  public int getUnocupiedSlotsForBlock(Block block) {
    int out = 0;
    for (Floor f : block.getFloors()) {
      int i = getUnocupiedSlotsForFloor(f);
      if (i == -1) return -1;
      out += i;
    }
    return out;
  }

  public int getUnocupiedSlotsForFloor(Floor floor) {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet rs =
          statement.executeQuery(
              "SELECT * FROM PARKING_SLOTS WHERE FLOOR_ID="
                  + floor.getId()
                  + " AND IS_OCCUPIED=false");
      int out = 0;
      while (rs.next()) {
        out++;
      }
      return out;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  void createTables() {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      statement.execute(
          "CREATE TABLE PARKING_LOTS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
              + "number_of_blocks integer,"
              + "name varchar(455),"
              + "is_reentry_allowed boolean,"
              + "is_vallet_available boolean,"
              + "is_lot_full boolean,"
              + "PRIMARY KEY (id))");
      statement.execute(
          "CREATE TABLE PARKING_BLOCKS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
              + "parking_lot_id integer,"
              + "block_code varchar(3),"
              + "number_of_floors integer,"
              + "is_block_full boolean,"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY (parking_lot_id) REFERENCES PARKING_LOTS(id))");
      statement.execute(
          "CREATE TABLE PARKING_FLOORS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
              + "block_id integer,"
              + "floor_number integer,"
              + "max_height_in_inch integer,"
              + "number_slots integer,"
              + "is_covered boolean,"
              + "is_accessible boolean,"
              + "is_floor_full boolean,"
              + "is_reserved_staff boolean,"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY (block_id) REFERENCES PARKING_BLOCKS(id))");
      statement.execute(
          "CREATE TABLE PARKING_SLOTS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
              + "slot_number integer,"
              + "code varchar(1),"
              + "is_occupied boolean,"
              + "floor_id integer,"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY (floor_id) REFERENCES PARKING_FLOORS(id))");

      statement.execute(
          "CREATE TABLE PARKING_CUSTOMERS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
              + "vehicle_licence varchar(8),"
              + "is_staff boolean,"
              + "contact_number varchar(10),"
              + "registration_date date,"
              + "PRIMARY KEY (id))");

      statement.execute(
          "CREATE TABLE PARKING_SLOT_RESERVATIONS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 10000, INCREMENT BY 1),"
              + "customer_id integer,"
              + "slot_id integer NOT NULL,"
              + "start_timestamp timestamp,"
              + "res_edn_timestamp timestamp,"
              + "booking_date date,"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY (customer_id) REFERENCES PARKING_CUSTOMERS(id),"
              + "FOREIGN KEY (slot_id) REFERENCES PARKING_SLOTS(id))");

      statement.execute(
          "CREATE TABLE STAFF_PERMITS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
              + "customer_id integer NOT NULL,"
              + "user_id integer NOT NULL,"
              + "reservation_id integer NOT NULL,"
              + "assignment_date date NOT NULL,"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY (customer_id) REFERENCES PARKING_CUSTOMERS(id),"
              + "FOREIGN KEY (reservation_id) REFERENCES PARKING_SLOT_RESERVATIONS(id))");

      statement.execute(
          "CREATE TABLE PARKING_SLIPS("
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 10000, INCREMENT BY 1),"
              + "reservation_id integer NOT NULL,"
              + "entry_timestamp timestamp NOT NULL,"
              + "exit_timestamp timestamp,"
              + "base_cost integer NOT NULL,"
              + "penalty integer,"
              + "is_paid boolean,"
              + "PRIMARY KEY (id),"
              + "FOREIGN KEY (reservation_id) REFERENCES PARKING_SLOT_RESERVATIONS(id))");

      statement.execute(
          "CREATE TABLE NAV_LINK("
              + "nav_id varchar(45) NOT NULL,"
              + "slot_id int NOT NULL,"
              + "FOREIGN KEY (slot_id) REFERENCES PARKING_SLOTS(id))");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  void dropTables() {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      statement.addBatch("DROP TABLE NAV_LINK");
      statement.addBatch("DROP TABLE STAFF_PERMITS");
      statement.addBatch("DROP TABLE PARKING_SLIPS");
      statement.addBatch("DROP TABLE PARKING_SLOT_RESERVATIONS");
      statement.addBatch("DROP TABLE PARKING_CUSTOMERS");
      statement.addBatch("DROP TABLE PARKING_SLOTS");
      statement.addBatch("DROP TABLE PARKING_FLOORS");
      statement.addBatch("DROP TABLE PARKING_BLOCKS");
      statement.addBatch("DROP TABLE PARKING_LOTS");
      statement.executeBatch();
    } catch (SQLException e) {
      // e.printStackTrace();
    }
  }

  void populateExampleData() {
    try {
      //      Statement statement = databaseRef.getConnection().createStatement();
      //      statement.addBatch(
      //          "INSERT INTO PARKING_LOTS(NUMBER_OF_BLOCKS, NAME, IS_REENTRY_ALLOWED,
      // IS_VALLET_AVAILABLE, IS_LOT_FULL) VALUES\n"
      //              + "(4, 'Western Parking', false, false, false)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_BLOCKS(parking_lot_id, block_code, number_of_floors,
      // is_block_full) VALUES\n"
      //              + "(1, 'WA', 1, false),\n"
      //              + "(1, 'WB', 1, false),\n"
      //              + "(1, 'WC', 1, false),\n"
      //              + "(1, 'WD', 1, false)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_FLOORS(block_id, floor_number, max_height_in_inch,
      // number_slots, is_covered, is_accessible, is_floor_full, is_reserved_staff) VALUES\n"
      //              + "(2, 1, 120, 8, true, true, false, false),\n"
      //              + "(2, 2, 120, 8, false, false, false, false),\n"
      //              + "(1, 1, 120, 8, false, true, false, true),\n"
      //              + "(3, 1, 120, 6, false, true, false, false),\n"
      //              + "(4, 1, 120, 6, false, true, false, false)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
      //              + "(101, 'A', false, 1),\n"
      //              + "(102, 'A', false, 1),\n"
      //              + "(103, 'A', false, 1),\n"
      //              + "(104, 'A', false, 1),\n"
      //              + "(105, 'A', false, 1),\n"
      //              + "(106, 'A', false, 1),\n"
      //              + "(107, 'A', false, 1),\n"
      //              + "(108, 'A', false, 1)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
      //              + "(101, 'B', false, 2),\n"
      //              + "(102, 'B', false, 2),\n"
      //              + "(103, 'B', false, 2),\n"
      //              + "(104, 'B', false, 2),\n"
      //              + "(105, 'B', false, 2),\n"
      //              + "(106, 'B', false, 2),\n"
      //              + "(107, 'B', false, 2),\n"
      //              + "(108, 'B', false, 2)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
      //              + "(201, 'B', false, 3),\n"
      //              + "(202, 'B', false, 3),\n"
      //              + "(203, 'B', false, 3),\n"
      //              + "(204, 'B', false, 3),\n"
      //              + "(205, 'B', false, 3),\n"
      //              + "(206, 'B', false, 3),\n"
      //              + "(207, 'B', false, 3),\n"
      //              + "(208, 'B', false, 3)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
      //              + "(101, 'C', false, 4),\n"
      //              + "(102, 'C', false, 4),\n"
      //              + "(103, 'C', false, 4),\n"
      //              + "(104, 'C', false, 4),\n"
      //              + "(105, 'C', false, 4),\n"
      //              + "(106, 'C', false, 4)");
      //      statement.addBatch(
      //          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
      //              + "(101, 'D', false, 5),\n"
      //              + "(102, 'D', false, 5),\n"
      //              + "(103, 'D', false, 5),\n"
      //              + "(104, 'D', false, 5),\n"
      //              + "(105, 'D', false, 5),\n"
      //              + "(106, 'D', false, 5)");
      //      statement.executeBatch();

      Lot eastSurfLot = createParkingLot("Eastern Surface Lot", false, true);
      addBlocksToLot(
          eastSurfLot, new Block("ESA"), new Block("ESB"), new Block("ESC"), new Block("ESD"));
      addFloorsToBlock(eastSurfLot.getBlocks().get(0), new Floor(1, false, true, false));
      List<Integer> eastSurfAIds =
          addSlotsToFloor(eastSurfLot.getBlocks().get(0).getFloors().get(0), 'A', 101, 20);
      for (int i = 0; i < NodeToParkLink.eastSurfALinks.length; i++) {
        addLink(NodeToParkLink.eastSurfALinks[i], eastSurfAIds.get(i));
      }

      addFloorsToBlock(eastSurfLot.getBlocks().get(1), new Floor(1, false, true, false));
      List<Integer> eastSurfBIds =
          addSlotsToFloor(eastSurfLot.getBlocks().get(1).getFloors().get(0), 'B', 101, 20);
      for (int i = 0; i < NodeToParkLink.eastSurfBLinks.length; i++) {
        addLink(NodeToParkLink.eastSurfBLinks[i], eastSurfBIds.get(i));
      }

      addFloorsToBlock(eastSurfLot.getBlocks().get(2), new Floor(1, false, true, false));
      List<Integer> eastSurfCIds =
          addSlotsToFloor(eastSurfLot.getBlocks().get(2).getFloors().get(0), 'C', 101, 20);
      for (int i = 0; i < NodeToParkLink.eastSurfCLinks.length; i++) {
        addLink(NodeToParkLink.eastSurfCLinks[i], eastSurfCIds.get(i));
      }

      addFloorsToBlock(eastSurfLot.getBlocks().get(3), new Floor(1, false, true, false));
      List<Integer> eastSurfDIds =
          addSlotsToFloor(eastSurfLot.getBlocks().get(3).getFloors().get(0), 'D', 101, 20);
      for (int i = 0; i < NodeToParkLink.eastSurfDLinks.length; i++) {
        addLink(NodeToParkLink.eastSurfDLinks[i], eastSurfDIds.get(i));
      }

      Lot eastLot = createParkingLot("Eastern Garage", false, false);
      addBlocksToLot(eastLot, new Block("MSP"));
      for (int i = 1; i <= 3; i++) {
        Floor f =
            addFloorsToBlock(eastLot.getBlocks().get(0), new Floor(i, i != 3, true, false)).get(0);
        addSlotsToFloor(f, 'E', i * 100 + 1, (i != 3) ? 46 : 47);
      }

      Lot westSurfLot = createParkingLot("Western Surface Lot", false, false);
      addBlocksToLot(westSurfLot, new Block("WSA"), new Block("WSB"), new Block("WSC"));
      for (Block b : westSurfLot.getBlocks()) {
        addFloorsToBlock(b, new Floor(1, false, true, false));
        addSlotsToFloor(b.getFloors().get(0), b.getBlockCode().charAt(2), 101, 43);
      }

      Lot westLot = createParkingLot("West Garage", false, false);
      addBlocksToLot(westLot, new Block("MSP"));
      for (int i = 1; i <= 5; i++) {
        Floor f =
            addFloorsToBlock(westLot.getBlocks().get(0), new Floor(i, i != 5, true, false)).get(0);
        addSlotsToFloor(f, 'W', i * 1000 + 1, 116);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    Lot westAdminLot = createParkingLot("West Admin Lot", true, false);
    addBlocksToLot(westAdminLot, new Block("WAL"));
    addFloorsToBlock(westAdminLot.getBlocks().get(0), new Floor(1, false, true, true));
    addSlotsToFloor(westAdminLot.getBlocks().get(0).getFloors().get(0), 'H', 101, 29);
  }

  public Lot createParkingLot(String name, boolean reentry, boolean vallet) {
    try {
      String query =
          "INSERT INTO PARKING_LOTS (NUMBER_OF_BLOCKS, NAME, IS_REENTRY_ALLOWED, IS_VALLET_AVAILABLE, IS_LOT_FULL) VALUES "
              + "(0, ?, ?, ?, false)";
      PreparedStatement statement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, name);
      statement.setBoolean(2, reentry);
      statement.setBoolean(3, vallet);

      statement.execute();
      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      int lotId = rs.getInt(1);
      return new Lot(lotId, name, reentry, vallet);
    } catch (SQLException e) {
      printSQLException(e);
      throw new IllegalStateException("Exception creating Lot");
    }
  }

  public void addLink(String navId, int slotId) {
    try {
      String query = "INSERT INTO NAV_LINK (NAV_ID, SLOT_ID) VALUES (?, ?)";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setString(1, navId);
      statement.setInt(2, slotId);

      statement.execute();
    } catch (SQLException e) {
      printSQLException(e);
      throw new IllegalStateException("Exception when adding a nav link");
    }
  }

  public int getLinkedSlotId(String navId) {
    try {
      String query = "SELECT * FROM NAV_LINK WHERE NAV_ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setString(1, navId);
      ResultSet rs = statement.executeQuery();

      if (rs.next()) return rs.getInt("SLOT_ID");
      else return -1;
    } catch (SQLException e) {
      printSQLException(e);
      throw new IllegalStateException("Exception when getting linked slot");
    }
  }

  public List<Block> addBlocksToLot(Lot lot, Block... blocks) {
    List<Block> out = new ArrayList<>();
    for (Block block : blocks) {
      try {
        String query =
            "INSERT INTO PARKING_BLOCKS (PARKING_LOT_ID, BLOCK_CODE, NUMBER_OF_FLOORS, IS_BLOCK_FULL) VALUES "
                + "(?, ?, 0, false)";
        PreparedStatement statement =
            databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, lot.getId());
        statement.setString(2, block.getBlockCode());

        statement.execute();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        block.setId(rs.getInt(1));
        lot.addBlock(block);
        out.add(block);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IllegalStateException("Exception adding blocks to lot");
      }
    }

    try {
      String query = "UPDATE PARKING_LOTS SET NUMBER_OF_BLOCKS = ? WHERE ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setInt(1, out.size());
      statement.setInt(2, lot.getId());

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Exception updating lot");
    }
    return out;
  }

  public List<Floor> addFloorsToBlock(Block block, Floor... floors) {
    List<Floor> out = new ArrayList<>();
    for (Floor floor : floors) {
      try {
        String query =
            "INSERT INTO PARKING_FLOORS (BLOCK_ID, FLOOR_NUMBER, MAX_HEIGHT_IN_INCH, NUMBER_SLOTS, IS_COVERED, IS_ACCESSIBLE, IS_FLOOR_FULL, IS_RESERVED_STAFF) VALUES "
                + "(?, ?, ?, 0, ?, ?, false, ?)";
        PreparedStatement statement =
            databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, block.getId());
        statement.setInt(2, floor.getFloorNum());
        statement.setInt(3, 72);
        statement.setBoolean(4, floor.isCovered());
        statement.setBoolean(5, floor.isDisabledAcessable());
        statement.setBoolean(6, floor.isStaffOnly());

        statement.execute();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        floor.setId(rs.getInt(1));
        block.addFloor(floor);
        out.add(floor);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IllegalStateException("Exception adding blocks to lot");
      }
    }

    try {
      String query = "UPDATE PARKING_BLOCKS SET NUMBER_OF_FLOORS = ? WHERE ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setInt(1, out.size());
      statement.setInt(2, block.getId());

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Exception updating block");
    }
    return out;
  }

  public List<Integer> addSlotsToFloor(Floor f, char code, int startAt, int numSlots) {
    List<Integer> out = new ArrayList<>();
    for (int slotNum = startAt; slotNum < startAt + numSlots; slotNum++) {
      try {
        String query =
            "INSERT INTO PARKING_SLOTS (SLOT_NUMBER, CODE, IS_OCCUPIED, FLOOR_ID) VALUES "
                + "(?, ?, false, ?)";
        PreparedStatement statement =
            databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, slotNum);
        statement.setString(2, code + "");
        statement.setInt(3, f.getId());

        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        out.add(rs.getInt(1));
        f.getSlotCodes().put("" + code + slotNum, rs.getInt(1));
      } catch (SQLException e) {
        printSQLException(e);
        throw new IllegalStateException("Exception when adding slots to floor");
      }
    }

    try {
      String query = "UPDATE PARKING_FLOORS SET NUMBER_SLOTS = ? WHERE ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setInt(1, out.size());
      statement.setInt(2, f.getId());

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Exception updating floor");
    }

    return out;
  }

  public StaffPermit getStaffPermitForUser(User user) {
    try {
      Statement statement = databaseRef.getConnection().createStatement();

      ResultSet rs =
          statement.executeQuery(
              "SELECT * FROM STAFF_PERMITS JOIN PARKING_SLOT_RESERVATIONS PSR on PSR.ID = STAFF_PERMITS.RESERVATION_ID WHERE USER_ID="
                  + user.getUserId());

      if (!rs.next()) {
        System.out.println("No permit for user id: " + user.getUserId());
        return null;
      }

      ParkingCustomer customer = getParkingCustomerForId(rs.getInt("customer_id"));

      return new StaffPermit(rs.getInt("SLOT_ID"), user, rs.getDate("assignment_date"), customer);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ParkingCustomer getParkingCustomerForId(int id) {
    try {
      String query = "SELECT * FROM PARKING_CUSTOMERS WHERE ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      if (!rs.next())
        throw new IllegalArgumentException("No Entry in Parking Customers for Id: " + id);

      return new ParkingCustomer(
          rs.getInt("ID"),
          rs.getString("vehicle_licence"),
          rs.getBoolean("is_staff"),
          rs.getString("contact_number"),
          rs.getDate("registration_date"));
    } catch (SQLException e) {
      printSQLException(e);
      throw new IllegalStateException("Failure on loading parking customer from database");
    }
  }

  public void requestStaffPermit(
      User user, String vehicleLicence, String contactNumber, Timestamp start, Timestamp end) {

    Date assignmentDate = new Date(System.currentTimeMillis());

    if (!user.hasPermission(User.Permission.REQUEST_PARKING_PERMIT))
      throw new IllegalArgumentException(
          "User did not have permission to request a staff parking permit");

    try {
      Statement statement = databaseRef.getConnection().createStatement();

      statement.execute("DELETE FROM STAFF_PERMITS WHERE USER_ID=" + user.getUserId());

    } catch (SQLException e) {
      e.printStackTrace();
    }

    int customerId = -1;
    try {
      String query =
          "INSERT INTO PARKING_CUSTOMERS(VEHICLE_LICENCE, IS_STAFF, CONTACT_NUMBER, REGISTRATION_DATE) VALUES "
              + "('"
              + vehicleLicence
              + "', true, '"
              + contactNumber
              + "', ?)";
      PreparedStatement statement =
          databaseRef
              .getConnection()
              .prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setDate(1, new Date(new java.util.Date().getTime()));
      statement.execute();

      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      customerId = rs.getInt(1);
      System.out.println("customer ID: " + customerId);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    int openSlotId = -1;

    try {

      Statement floorStatement = databaseRef.getConnection().createStatement();
      ResultSet floorResults =
          floorStatement.executeQuery("SELECT ID FROM PARKING_FLOORS WHERE IS_RESERVED_STAFF=true");

      while (floorResults.next()) {

        Statement slotStatement = databaseRef.getConnection().createStatement();
        ResultSet slotResults =
            slotStatement.executeQuery(
                "SELECT ps.id FROM PARKING_SLOTS ps LEFT JOIN PARKING_SLOT_RESERVATIONS PSR on ps.ID = PSR.SLOT_ID WHERE PSR.SLOT_ID IS NULL AND PS.FLOOR_ID="
                    + floorResults.getInt("id"));

        if (slotResults.next()) {
          openSlotId = slotResults.getInt(1);
          System.out.println("openSlotId ID: " + openSlotId);
          break;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (openSlotId == -1) {
      throw new IllegalStateException("No open staff only slots to assign to staff member");
    }

    int reservationId = -1;
    try {
      String query =
          "INSERT INTO PARKING_SLOT_RESERVATIONS (CUSTOMER_ID, SLOT_ID, START_TIMESTAMP, RES_EDN_TIMESTAMP) VALUES "
              + "("
              + customerId
              + ", "
              + openSlotId
              + ", ?, ?)";
      PreparedStatement statement =
          databaseRef
              .getConnection()
              .prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setTimestamp(1, start);
      statement.setTimestamp(2, end);

      statement.execute();

      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      reservationId = rs.getInt(1);
      System.out.println("reservation ID: " + reservationId);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      String query =
          "INSERT INTO STAFF_PERMITS(CUSTOMER_ID, USER_ID, RESERVATION_ID, ASSIGNMENT_DATE) VALUES "
              + "("
              + customerId
              + ", "
              + user.getUserId()
              + ", "
              + reservationId
              + ", ?)";

      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setDate(1, assignmentDate);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a new Slip
   *
   * @param startTime when the reserved slip should start will also be the entry time
   * @param durationMin length of slip in minutes
   * @param cost cost of ticket in cents
   * @return the new Slip
   */
  public ParkingSlip createNewSlip(Timestamp startTime, int durationMin, int cost) {

    ParkingReservation reservation =
        createNewReservation(
            null,
            startTime,
            new Timestamp(startTime.getTime() + TimeUnit.MINUTES.toMillis((durationMin))));

    int createdSlip = -1;

    try {
      String query =
          "INSERT INTO PARKING_SLIPS (RESERVATION_ID, ENTRY_TIMESTAMP, BASE_COST, IS_PAID) VALUES (?, ?, ?, ?)";
      PreparedStatement preparedStatement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

      preparedStatement.setInt(1, reservation.getId());
      preparedStatement.setTimestamp(2, startTime);
      preparedStatement.setInt(3, cost);
      preparedStatement.setBoolean(4, false);

      preparedStatement.execute();

      ResultSet rs = preparedStatement.getGeneratedKeys();
      rs.next();
      createdSlip = rs.getInt(1);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (createdSlip == -1) throw new IllegalStateException("Failed to add parking slip to DB");

    return new ParkingSlip(createdSlip, reservation, startTime, durationMin, cost / 100.0);
  }

  public ParkingCustomer createNewCustomer(
      String vehicleLicence, boolean isStaff, String contactNumber) {
    try {
      String query =
          "INSERT INTO PARKING_CUSTOMERS (VEHICLE_LICENCE, IS_STAFF, CONTACT_NUMBER, REGISTRATION_DATE) VALUES (?, ?, ?, ?)";
      PreparedStatement statement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

      Date regDate = new Date(System.currentTimeMillis());

      statement.setString(1, vehicleLicence);
      statement.setBoolean(2, isStaff);
      statement.setString(3, contactNumber);
      statement.setDate(4, regDate);

      statement.execute();
      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      int customerId = rs.getInt(1);
      return new ParkingCustomer(customerId, vehicleLicence, isStaff, contactNumber, regDate);

    } catch (SQLException e) {
      printSQLException(e);
      throw new IllegalStateException("Error thrown inserting new Customer");
    }
  }

  public int getSlotIdForTime(Timestamp startTime, Timestamp endTime) {
    try {
      String slotQuery =
          "SELECT * FROM PARKING_SLOTS ps LEFT JOIN (SELECT P.ID, P.SLOT_ID FROM PARKING_SLOT_RESERVATIONS P WHERE NOT (((P.START_TIMESTAMP>=? AND P.RES_EDN_TIMESTAMP>=?) OR (P.START_TIMESTAMP<=? AND P.RES_EDN_TIMESTAMP<=?)))) as Pres on ps.ID = Pres.SLOT_ID WHERE Pres.ID IS NULL AND ps.IS_OCCUPIED=false";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(slotQuery);
      statement.setTimestamp(1, startTime);
      statement.setTimestamp(2, startTime);
      statement.setTimestamp(3, endTime);
      statement.setTimestamp(4, endTime);
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        return rs.getInt("ID");
      } else return -1;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Error thrown retrieving slotID for time");
    }
  }

  public String getSlotCodeForTime(Timestamp startTime, Timestamp endTime) {
    try {
      String slotQuery =
          "SELECT * FROM PARKING_SLOTS ps LEFT JOIN (SELECT P.ID, P.SLOT_ID FROM PARKING_SLOT_RESERVATIONS P WHERE NOT (((P.START_TIMESTAMP>=? AND P.RES_EDN_TIMESTAMP>=?) OR (P.START_TIMESTAMP<=? AND P.RES_EDN_TIMESTAMP<=?)))) as Pres on ps.ID = Pres.SLOT_ID WHERE Pres.ID IS NULL AND ps.IS_OCCUPIED=false";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(slotQuery);
      statement.setTimestamp(1, startTime);
      statement.setTimestamp(2, startTime);
      statement.setTimestamp(3, endTime);
      statement.setTimestamp(4, endTime);
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        return rs.getString("CODE") + rs.getString("SLOT_NUMBER");
      } else return null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Error thrown retrieving slotCode for time");
    }
  }

  public ParkingReservation createNewReservation(
      ParkingCustomer customer, Timestamp startTime, Timestamp endTime) {
    int slotId = -1;
    String slotCode = null;

    slotId = getSlotIdForTime(startTime, endTime);
    slotCode = getSlotCodeForTime(startTime, endTime);

    if (slotId == -1 || slotCode == null)
      throw new IllegalStateException("Failed to find empty slot when printing ticket");

    System.out.println(slotId + ": " + slotCode);

    int createdReservation = -1;

    Date bookingDate = new Date(System.currentTimeMillis());

    try {
      String query =
          "INSERT INTO PARKING_SLOT_RESERVATIONS (SLOT_ID, START_TIMESTAMP, RES_EDN_TIMESTAMP, BOOKING_DATE"
              + ((customer == null) ? "" : ", CUSTOMER_ID")
              + ") VALUES "
              + "(?, ?, ?, ?"
              + ((customer == null) ? "" : ", ?")
              + ")";
      PreparedStatement statement =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      statement.setInt(1, slotId);
      statement.setTimestamp(2, startTime);
      statement.setTimestamp(3, endTime);
      statement.setDate(4, bookingDate);
      if (customer != null) statement.setInt(5, customer.getId());

      statement.execute();
      ResultSet rs = statement.getGeneratedKeys();
      rs.next();
      createdReservation = rs.getInt(1);
    } catch (SQLException e) {
      printSQLException(e);
      throw new IllegalStateException("Exception thrown when attemtping to create new Reservation");
    }

    if (customer == null)
      return new ParkingReservation(
          createdReservation, null, slotId, slotCode, startTime, endTime, bookingDate);
    else
      return new ParkingReservation(
          createdReservation, customer, slotId, slotCode, startTime, endTime, bookingDate);
  }

  public void updateSlipPenalty(int id, double penalty) {
    try {
      int p = (int) (penalty * 100.0);

      String query = "UPDATE PARKING_SLIPS SET PENALTY = ? WHERE ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setInt(1, p);
      statement.setInt(2, id);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Integer> getCurrentParkingSlips() {
    List<Integer> slips = new ArrayList<>();
    try {
      String query = "SELECT * FROM PARKING_SLIPS";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        slips.add(rs.getInt("ID"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return slips;
  }

  public ParkingReservation getReservationForId(int Id) {
    try {
      String query = "SELECT * FROM PARKING_SLOT_RESERVATIONS WHERE ID = ?";
      PreparedStatement stmt = databaseRef.getConnection().prepareStatement(query);
      stmt.setInt(1, Id);
      ResultSet rs = stmt.executeQuery();

      ParkingReservation resv;
      if (rs.next()) {
        Timestamp startTime = rs.getTimestamp("start_timestamp");
        Timestamp endTime = rs.getTimestamp("res_edn_timestamp");
        resv =
            new ParkingReservation(
                Id,
                getParkingCustomerForId(rs.getInt("customer_ID")),
                rs.getInt("slot_ID"),
                getSlotCodeForTime(startTime, endTime),
                startTime,
                endTime,
                rs.getDate("booking_Date"));
        return resv;
      } else return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void slipPaid(int id) {
    try {

      String query = "UPDATE PARKING_SLIPS SET EXIT_TIMESTAMP = ?, IS_PAID = true WHERE ID = ?";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      statement.setInt(2, id);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Integer> getOccupiedSlots() {
    List<Integer> out = new ArrayList<>();
    try {
      String query = "SELECT * FROM PARKING_SLOTS WHERE IS_OCCUPIED = true";
      PreparedStatement statement = databaseRef.getConnection().prepareStatement(query);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        out.add(rs.getInt("ID"));
      }
    } catch (SQLException e) {
      printSQLException(e);
    }
    return out;
  }
}
