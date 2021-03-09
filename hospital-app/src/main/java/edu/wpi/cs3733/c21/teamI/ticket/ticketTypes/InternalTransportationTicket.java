package edu.wpi.cs3733.c21.teamI.ticket.ticketTypes;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;

public class InternalTransportationTicket extends ServiceTicket {
  private String pickUpDate;
  private String pickUpTime;
  private String destination;
  private boolean emergency;
  private boolean stretcher;
  private boolean wheelchair;

  public InternalTransportationTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String pickUpDate,
      String pickUpTime,
      String destination,
      boolean emergency,
      boolean stretcher,
      boolean wheelchair) {
    super(requestID, TicketType.INTERNAL_TRANSPORTATION, location, desc, completed);
    this.pickUpDate = pickUpDate;
    this.pickUpTime = pickUpTime;
    this.destination = destination;
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

  public String getDestination() {
    return destination;
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
