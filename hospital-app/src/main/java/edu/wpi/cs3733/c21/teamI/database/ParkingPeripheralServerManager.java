package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.parking.Block;
import edu.wpi.cs3733.c21.teamI.parking.Floor;
import edu.wpi.cs3733.c21.teamI.parking.Lot;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.apache.derby.drda.NetworkServerControl;

public class ParkingPeripheralServerManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby://localhost:1527/peripheralDB";

  private static ParkingPeripheralServerManager ourInstance;

  public static void init(boolean regen) {
    try {
      NetworkServerControl control = new NetworkServerControl();
      control.start(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
      System.out.println("Hello WORLD");
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  void dropTables() {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      statement.addBatch("DROP TABLE PARKING_SLOTS");
      statement.addBatch("DROP TABLE PARKING_FLOORS");
      statement.addBatch("DROP TABLE PARKING_BLOCKS");
      statement.addBatch("DROP TABLE PARKING_LOTS");
      statement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
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
}
