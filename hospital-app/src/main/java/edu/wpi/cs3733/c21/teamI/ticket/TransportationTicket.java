package edu.wpi.cs3733.c21.teamI.ticket;

public class TransportationTicket extends ServiceTicket {
  private String pickUpDate;
  private String pickUpTime;
  private boolean emergency;
  private boolean stretcher;
  private boolean wheelchair;

  public TransportationTicket(
      int requestID,
      int assignID,
      String location,
      String desc,
      boolean completed,
      String pickUpDate,
      String pickUpTime,
      boolean emergency,
      boolean stretcher,
      boolean wheelchair) {
    super(requestID, assignID, TicketType.TRANSPORTATION, location, desc, completed);
    this.pickUpDate = pickUpDate;
    this.pickUpTime = pickUpTime;
    this.emergency = emergency;
    this.stretcher = stretcher;
    this.wheelchair = wheelchair;
  }

  public String getPickUpDate() {
    return pickUpDate;
  }

  public String getPickUpTime() {
    return pickUpTime;
  }

  public boolean isEmergency() {
    return emergency;
  }

  public boolean isStretcher() {
    return stretcher;
  }

  public boolean isWheelchair() {
    return wheelchair;
  }
}
