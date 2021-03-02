package edu.wpi.cs3733.c21.teamI.ticket;

public class LaundryTicket extends ServiceTicket {
  private String pickUpDate;
  private String pickUpTime;
  private boolean dryClean;

  public LaundryTicket(
      int requestID,
      int assignID,
      String location,
      String desc,
      boolean completed,
      String pickUpDate,
      String pickUpTime,
      boolean dryClean) {
    super(requestID, TicketType.LAUNDRY, location, desc, completed);
    this.pickUpDate = pickUpDate;
    this.pickUpTime = pickUpTime;
    this.dryClean = dryClean;
  }

  public String getPickUpDate() {
    return pickUpDate;
  }

  public String getPickUpTime() {
    return pickUpTime;
  }

  public boolean isDryClean() {
    return dryClean;
  }
}
