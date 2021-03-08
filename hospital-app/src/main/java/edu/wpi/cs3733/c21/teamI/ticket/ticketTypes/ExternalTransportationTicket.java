package edu.wpi.cs3733.c21.teamI.ticket;

public class ExternalTransportationTicket extends ServiceTicket {
  private String pickUpDate;
  private String pickUpTime;
  private String destination;

  public ExternalTransportationTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String pickUpDate,
      String pickUpTime,
      String destination) {
    super(requestID, TicketType.INTERNAL_TRANSPORTATION, location, desc, completed);
    this.pickUpDate = pickUpDate;
    this.pickUpTime = pickUpTime;
    this.destination = destination;
  }

  public String getPickUpDate() {
    return pickUpDate;
  }

  public String getPickUpTime() {
    return pickUpTime;
  }

  public String getDestination() {
    return destination;
  }
}
