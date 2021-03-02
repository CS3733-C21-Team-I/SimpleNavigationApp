package edu.wpi.cs3733.c21.teamI.ticket;

public class ComputerTicket extends ServiceTicket {
  private String computerType;
  private boolean urgency;

  public ComputerTicket(
      int requestID,
      int assignID,
      String location,
      String desc,
      boolean completed,
      String computerType,
      boolean urgency) {
    super(requestID, TicketType.COMPUTER, location, desc, completed);
    this.computerType = computerType;
    this.urgency = urgency;
  }

  public String getComputerType() {
    return computerType;
  }

  public boolean isUrgency() {
    return urgency;
  }
}
