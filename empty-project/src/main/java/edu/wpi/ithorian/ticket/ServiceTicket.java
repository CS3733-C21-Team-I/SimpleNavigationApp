package edu.wpi.ithorian.ticket;

import edu.wpi.ithorian.hospitalMap.LocationNode;

public class ServiceTicket {

  private int ticketId;
  private int requestingUserID;
  private int assignedUserID;
  private TicketType ticketType;
  private LocationNode location;
  private String description;
  private boolean completed;

  public ServiceTicket() {}

  public ServiceTicket(
      int requestID,
      int assignID,
      TicketType ticketType,
      LocationNode location,
      String desc,
      boolean complete) {
    this.requestingUserID = requestID;
    this.assignedUserID = assignID;
    this.ticketType = ticketType;
    this.location = location;
    this.description = desc;
    this.completed = complete;
  }

  public enum TicketType {
    LAUNDRY,
    FOOD,
    SECURITY,
    MAINTENANCE
  }

  @Override
  public String toString() {
    return "NO. "
        + ticketId
        + " FR0M: "
        + requestingUserID
        + " TO: "
        + assignedUserID
        + "\nTYPE: "
        + ticketType
        + "\nAT: "
        + location.toString()
        + "\nDESCPRIPTION: "
        + description
        + " COMPLETED: "
        + completed;
  }
}
