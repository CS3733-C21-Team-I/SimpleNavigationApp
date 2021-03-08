package edu.wpi.cs3733.c21.teamI.ticket.ticketTypes;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;

public class ComputerTicket extends ServiceTicket {
  private String computerType;
  private boolean urgency;

  public ComputerTicket(
      int requestID,
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
