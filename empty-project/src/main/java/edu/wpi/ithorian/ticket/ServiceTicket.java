package edu.wpi.ithorian.ticket;

public class ServiceTicket {

  private int ticketId;
  private int requestingUserID;
  private int assignedUserID;
  private TicketType ticketType;

  private String location;
  private String description;
  private boolean completed;

  public ServiceTicket() {}

  public ServiceTicket(
      int ticketId,
      int requestID,
      int assignID,
      TicketType ticketType,
      String location,
      String desc,
      boolean complete) {
    this.ticketId = ticketId;
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
    return " NO. "
        + ticketId
        + " FR0M: "
        + requestingUserID
        + " TO: "
        + assignedUserID
        + "\nTYPE: "
        + ticketType
        + "\nAT: "
        + location
        + "\nDESCPRIPTION: "
        + description
        + " COMPLETED: "
        + completed;
  }

  public int getRequestingUserID() {
    return requestingUserID;
  }

  public int getAssignedUserID() {
    return assignedUserID;
  }

  public TicketType getTicketType() {
    return ticketType;
  }

  public String getLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

}
