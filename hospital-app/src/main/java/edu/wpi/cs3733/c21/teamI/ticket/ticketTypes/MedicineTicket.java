package edu.wpi.cs3733.c21.teamI.ticket.ticketTypes;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;

public class MedicineTicket extends ServiceTicket {
  private String patientName;
  private String drugName;
  private String dose;
  private String date;
  private String time;

  public MedicineTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String patientName,
      String drugName,
      String dose,
      String date,
      String time) {
    super(requestID, TicketType.MEDICINE, location, desc, completed);
    this.patientName = patientName;
    this.drugName = drugName;
    this.dose = dose;
    this.date = date;
    this.time = time;
  }

  public String getPatientName() {
    return patientName;
  }

  public String getDrugName() {
    return drugName;
  }

  public String getDose() {
    return dose;
  }

  public String getDate() {
    return date;
  }

  public String getTime() {
    return time;
  }
}
