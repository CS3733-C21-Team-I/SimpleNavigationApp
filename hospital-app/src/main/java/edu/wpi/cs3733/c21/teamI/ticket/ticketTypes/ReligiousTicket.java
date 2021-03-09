package edu.wpi.cs3733.c21.teamI.ticket.ticketTypes;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;

public class ReligiousTicket extends ServiceTicket {
  private String patientName;
  private String religiousDenomination;
  private String religiousType;
  private String date;
  private String time;

  public ReligiousTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String patientName,
      String religiousDenomination,
      String religiousType,
      String date,
      String time) {
    super(requestID, TicketType.RELIGIOUS, location, desc, completed);
    this.patientName = patientName;
    this.religiousDenomination = religiousDenomination;
    this.religiousType = religiousType;
    this.date = date;
    this.time = time;
  }

  public String getPatientName() {
    return patientName;
  }

  public String getReligiousDenomination() {
    return religiousDenomination;
  }

  public String getReligiousType() {
    return religiousType;
  }

  public String getDate() {
    return date;
  }

  public String getTime() {
    return time;
  }
}
