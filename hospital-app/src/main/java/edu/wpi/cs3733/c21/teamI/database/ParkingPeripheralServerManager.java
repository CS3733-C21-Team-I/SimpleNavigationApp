package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.parking.Block;
import edu.wpi.cs3733.c21.teamI.parking.Floor;
import edu.wpi.cs3733.c21.teamI.parking.Lot;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingCustomer;
import edu.wpi.cs3733.c21.teamI.parking.reservations.StaffPermit;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
              + "id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
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
              + "FOREIGN KEY (slot_id) REFERENCES PARKING_SLOTS(id))");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  void dropTables() {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
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
      Statement statement = databaseRef.getConnection().createStatement();
      statement.addBatch(
          "INSERT INTO PARKING_LOTS(NUMBER_OF_BLOCKS, NAME, IS_REENTRY_ALLOWED, IS_VALLET_AVAILABLE, IS_LOT_FULL) VALUES\n"
              + "(4, 'Western Parking', false, false, false)");
      statement.addBatch(
          "INSERT INTO PARKING_BLOCKS(parking_lot_id, block_code, number_of_floors, is_block_full) VALUES\n"
              + "(1, 'WA', 1, false),\n"
              + "(1, 'WB', 1, false),\n"
              + "(1, 'WC', 1, false),\n"
              + "(1, 'WD', 1, false)");
      statement.addBatch(
          "INSERT INTO PARKING_FLOORS(block_id, floor_number, max_height_in_inch, number_slots, is_covered, is_accessible, is_floor_full, is_reserved_staff) VALUES\n"
              + "(2, 1, 120, 8, true, true, false, false),\n"
              + "(2, 2, 120, 8, false, false, false, false),\n"
              + "(1, 1, 120, 8, false, true, false, true),\n"
              + "(3, 1, 120, 6, false, true, false, false),\n"
              + "(4, 1, 120, 6, false, true, false, false)");
      statement.addBatch(
          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
              + "(101, 'A', false, 1),\n"
              + "(102, 'A', false, 1),\n"
              + "(103, 'A', false, 1),\n"
              + "(104, 'A', false, 1),\n"
              + "(105, 'A', false, 1),\n"
              + "(106, 'A', false, 1),\n"
              + "(107, 'A', false, 1),\n"
              + "(108, 'A', false, 1)");
      statement.addBatch(
          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
              + "(101, 'B', false, 2),\n"
              + "(102, 'B', false, 2),\n"
              + "(103, 'B', false, 2),\n"
              + "(104, 'B', false, 2),\n"
              + "(105, 'B', false, 2),\n"
              + "(106, 'B', false, 2),\n"
              + "(107, 'B', false, 2),\n"
              + "(108, 'B', false, 2)");
      statement.addBatch(
          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
              + "(201, 'B', false, 3),\n"
              + "(202, 'B', false, 3),\n"
              + "(203, 'B', false, 3),\n"
              + "(204, 'B', false, 3),\n"
              + "(205, 'B', false, 3),\n"
              + "(206, 'B', false, 3),\n"
              + "(207, 'B', false, 3),\n"
              + "(208, 'B', false, 3)");
      statement.addBatch(
          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
              + "(101, 'C', false, 4),\n"
              + "(102, 'C', false, 4),\n"
              + "(103, 'C', false, 4),\n"
              + "(104, 'C', false, 4),\n"
              + "(105, 'C', false, 4),\n"
              + "(106, 'C', false, 4)");
      statement.addBatch(
          "INSERT INTO PARKING_SLOTS(slot_number, code, is_occupied, floor_id) VALUES\n"
              + "(101, 'D', false, 5),\n"
              + "(102, 'D', false, 5),\n"
              + "(103, 'D', false, 5),\n"
              + "(104, 'D', false, 5),\n"
              + "(105, 'D', false, 5),\n"
              + "(106, 'D', false, 5)");
      statement.executeBatch();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public StaffPermit getStaffPermitForUser(User user) {
    try {
      Statement statement = databaseRef.getConnection().createStatement();

      ResultSet rs =
          statement.executeQuery(
              "SELECT * FROM STAFF_PERMITS JOIN PARKING_CUSTOMERS PC on PC.ID = STAFF_PERMITS.CUSTOMER_ID JOIN PARKING_SLOT_RESERVATIONS PSR on PSR.ID = STAFF_PERMITS.RESERVATION_ID WHERE USER_ID="
                  + user.getUserId());

      if (!rs.next()) {
        System.out.println("No permit for user id: " + user.getUserId());
        return null;
      }

      return new StaffPermit(
          rs.getInt("SLOT_ID"),
          user,
          rs.getDate("assignment_date"),
          new ParkingCustomer(
              rs.getString("vehicle_licence"),
              rs.getBoolean("is_staff"),
              rs.getString("contact_number"),
              rs.getDate("registration_date")));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void requestStaffPermit(
      User user, String vehicleLicence, String contactNumber, Date assignmentDate) {

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
          "INSERT INTO PARKING_SLOT_RESERVATIONS (CUSTOMER_ID, SLOT_ID) VALUES "
              + "("
              + customerId
              + ", "
              + openSlotId
              + ")";
      PreparedStatement statement =
          databaseRef
              .getConnection()
              .prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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

    public ParkingSlip createNewSlip(Timestamp startTime, Timestamp endTime, int cost) {
        int slotId = -1;
        String slotCode = null;

        try {
            String slotQuery =
                    "SELECT * FROM PARKING_SLOTS ps LEFT JOIN (SELECT P.ID, P.SLOT_ID FROM PARKING_SLIPS P WHERE NOT (((P.ENTRY_TIMESTAMP>=? AND P.ENTRY_TIMESTAMP>=?) OR (P.EXIT_TIMESTAMP<=? AND P.EXIT_TIMESTAMP<=?)))) as Pslip on ps.ID = Pslip.SLOT_ID WHERE Pslip.ID IS NULL AND ps.IS_OCCUPIED=false";
            PreparedStatement statement = databaseRef.getConnection().prepareStatement(slotQuery);

            statement.setTimestamp(1, startTime);
            statement.setTimestamp(2, endTime);
            statement.setTimestamp(3, startTime);
            statement.setTimestamp(4, endTime);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                slotId = rs.getInt("ID");
                slotCode = rs.getString("CODE") + rs.getString("SLOT_NUMBER");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (slotId == -1 || slotCode == null)
            throw new IllegalStateException("Failed to find empty slot when printing ticket");

        System.out.println(slotId + ": " + slotCode);

        int createdSlip = -1;

        try {
            String query =
                    "INSERT INTO PARKING_SLIPS (SLOT_ID, ENTRY_TIMESTAMP, EXIT_TIMESTAMP, BASE_COST, IS_PAID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement =
                    databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, slotId);
            preparedStatement.setTimestamp(2, startTime);
            preparedStatement.setTimestamp(3, endTime);
            preparedStatement.setInt(4, cost);
            preparedStatement.setBoolean(5, false);

            preparedStatement.execute();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            createdSlip = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (createdSlip == -1) throw new IllegalStateException("Failed to add parking slip to DB");

        return new ParkingSlip(createdSlip, slotId, slotCode, startTime, endTime, cost / 100.0);
    }
}
