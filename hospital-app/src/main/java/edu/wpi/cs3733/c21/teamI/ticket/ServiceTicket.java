package edu.wpi.cs3733.c21.teamI.ticket;

public class ServiceTicket {

  private int ticketID;
  private int requestingUserID;
  private int assignedUserID;
  private TicketType ticketType;
  private String location;
  private String description;
  private boolean completed;
  private boolean emergency;

  public ServiceTicket() {}

  public ServiceTicket(
      int requestID,
      int assignID,
      TicketType ticketType,
      String location,
      String desc,
      boolean emergency,
      boolean completed) {
    this.requestingUserID = requestID;
    this.assignedUserID = assignID;
    this.ticketType = ticketType;
    this.location = location;
    this.description = desc;
    this.emergency = emergency;
    this.completed = completed;
  }

  public enum TicketType {
    LAUNDRY,
    FOOD,
    SECURITY,
    SANITATION,
    MAINTENANCE
  }

  @Override
  public String toString() {
    return " FR0M: "
        + requestingUserID
        + "\tTO: "
        + assignedUserID
        + "\nTYPE: "
        + ticketType
        + "\nAT: "
        + location
        + "\nDESCPRIPTION: "
        + description
        + "\nEMERGENCY: "
        + emergency
        + "\tCOMPLETED: "
        + completed;
  }

  public int getRequestingUserID() {
    return requestingUserID;
  }

  public int getTicketId() {
    return ticketID;
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

  public boolean isEmergency() {
    return emergency;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(Boolean bool) {
    this.completed = bool;
  }

  public void setTicketID(int id) {
    this.ticketID = id;
  }
}
