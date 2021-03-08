package edu.wpi.cs3733.c21.teamI.ticket;

public class SecurityTicket extends ServiceTicket {
  private String securityType;
  private boolean emergency;

  public SecurityTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String securityType,
      boolean emergency) {
    super(requestID, TicketType.SECURITY, location, desc, completed);
    this.securityType = securityType;
    this.emergency = emergency;
  }

  public String getSecurityType() {
    return securityType;
  }

  public boolean isEmergency() {
    return emergency;
  }
}