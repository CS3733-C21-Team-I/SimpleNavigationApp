package edu.wpi.cs3733.c21.teamI.database;

import java.sql.SQLException;
import java.sql.Statement;
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
}
