package edu.wpi.cs3733.c21.teamI.parking.reservations;

import java.sql.Date;
import java.sql.Timestamp;

public class ParkingReservation {
  private int id;
  private ParkingCustomer customer;
  private int slotId;
  private String slotCode;
  private Timestamp startTimestamp;
  private Timestamp endTimestamp;
  private Date bookingDate;

  public ParkingReservation(
      int id,
      ParkingCustomer customer,
      int slotId,
      String slotCode,
      Timestamp startTimestamp,
      Timestamp endTimestamp,
      Date bookingDate) {
    this.id = id;
    this.customer = customer;
    this.slotId = slotId;
    this.slotCode = slotCode;
    this.startTimestamp = startTimestamp;
    this.endTimestamp = endTimestamp;
    this.bookingDate = bookingDate;
  }

  public int getId() {
    return id;
  }

  public ParkingCustomer getCustomer() {
    return customer;
  }

  public int getSlotId() {
    return slotId;
  }

  public String getSlotCode() {
    return slotCode;
  }

  public Timestamp getStartTimestamp() {
    return startTimestamp;
  }

  public Timestamp getEndTimestamp() {
    return endTimestamp;
  }

  public Date getBookingDate() {
    return bookingDate;
  }
}
