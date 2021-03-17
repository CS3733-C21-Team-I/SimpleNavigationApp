package edu.wpi.cs3733.c21.teamI.parking.reservations;

import edu.wpi.cs3733.c21.teamI.serial.ArduinoSlipSerial;
import edu.wpi.cs3733.c21.teamI.serial.SlipSerialInterface;
import java.time.format.DateTimeFormatter;

public class PeripheralSlipManager {

  private static PeripheralSlipManager ourInstance;

  public static PeripheralSlipManager getInstance() {
    return ourInstance;
  }

  public static void init(String[] comPorts) {
    ourInstance = new PeripheralSlipManager(comPorts);
  }

  private SlipSerialInterface serial;
  private boolean isConnected;

  private PeripheralSlipManager(String[] comPorts) {
    if (comPorts.length == 0) {
      isConnected = false;
      return;
    }

    isConnected = true;
    serial = new ArduinoSlipSerial();
    serial.initialize(comPorts);
  }

  public void addTicket(ParkingSlip slip) {

    String date =
        slip.getEntryTimestamp()
            .toLocalDateTime()
            .toLocalDate()
            .format(isConnected ? serial.getDateFormat() : DateTimeFormatter.ISO_DATE);

    System.out.println(date);

    String startTime =
        slip.getEntryTimestamp()
            .toLocalDateTime()
            .toLocalTime()
            .format(isConnected ? serial.getTimeFormat() : DateTimeFormatter.ISO_TIME);
    System.out.println(startTime);

    String endTime =
        slip.getEndTimestamp()
            .toLocalDateTime()
            .toLocalTime()
            .format(isConnected ? serial.getTimeFormat() : DateTimeFormatter.ISO_TIME);
    System.out.println(endTime);

    String cost = serial.getCostFormat().format(slip.getBaseCost());
    System.out.println(cost);

    System.out.println(String.valueOf(slip.getId()));

    if (isConnected) {
      serial.printTicket(
          slip.getReservation().getSlotCode(),
          date,
          startTime,
          endTime,
          cost,
          String.valueOf(slip.getId()));

    } else {
      System.out.println("No printer connected when attempting to print ticket: ");
      System.out.println(slip);
    }
  }
}
