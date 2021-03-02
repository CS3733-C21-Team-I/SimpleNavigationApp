package edu.wpi.cs3733.c21.teamI.ticket;

import java.util.List;

public class ServiceTicket {

  private int ticketID;
  private int requestingUserID;
  private List<Integer> assignedUserID;
  private TicketType ticketType;
  private String location;
  private String description;
  private boolean completed;
  // private String requestType;
  // private boolean emergency;

  public ServiceTicket() {}

  public ServiceTicket(
      int requestID, TicketType ticketType, String location, String desc, boolean completed) {
    this.requestingUserID = requestID;
    // this.requestType = requestType;
    this.ticketType = ticketType;
    this.location = location;
    this.description = desc;
    // this.emergency = emergency;
    this.completed = completed;
  }

  public enum TicketType {
    AUDIO_VISUAL,
    COMPUTER,
    LANGUAGE,
    PARKING,
    FLORAL,
    GIFT,
    TRANSPORTATION,
    LAUNDRY,
    MEDICINE,
    RELIGIOUS,
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
        + "\tCOMPLETED: "
        + completed;
  }

  public int getRequestingUserID() {
    return requestingUserID;
  }

  public int getTicketId() {
    return ticketID;
  }

  public List<Integer> getAssignedUserID() {
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

  public void setCompleted(Boolean bool) {
    this.completed = bool;
  }

  public void setTicketID(int id) {
    this.ticketID = id;
  }

  public void setAssignedUserID(List<Integer> employee) {
    this.assignedUserID = employee;
  }

  public void addAssignedUserID(int employee) {
    assignedUserID.add(employee);
  }

  //  public String getRequestType() {
  //    return requestType;
  //  }
  //  public boolean isEmergency() {
  //    return emergency;
  //  }
}
