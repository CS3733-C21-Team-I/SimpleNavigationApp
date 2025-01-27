package edu.wpi.cs3733.c21.teamI.ticket.ticketTypes;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;

public class FloralTicket extends ServiceTicket {
  private String patientName;
  private String deliveryDate;
  private String deliveryTime;

  public FloralTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String patientName,
      String deliveryDate,
      String deliveryTime) {
    super(requestID, TicketType.FLORAL, location, desc, completed);
    this.patientName = patientName;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
  }

  public String getPatientName() {
    return patientName;
  }

  public String getDeliveryDate() {
    return deliveryDate;
  }

  public String getDeliveryTime() {
    return deliveryTime;
  }
}
