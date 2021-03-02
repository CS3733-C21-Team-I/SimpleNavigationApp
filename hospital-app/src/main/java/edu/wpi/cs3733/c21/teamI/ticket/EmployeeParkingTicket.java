package edu.wpi.cs3733.c21.teamI.ticket;

public class EmployeeParkingTicket extends ServiceTicket {
  private String licensePlate;
  private int contact;
  private String startDate;
  private String endDate;
  private boolean disability;

  public EmployeeParkingTicket(
      int requestID,
      int assignID,
      String location,
      String desc,
      boolean completed,
      String licensePlate,
      int contact,
      String startDate,
      String endDate,
      boolean disability) {
    super(requestID, assignID, TicketType.PARKING, location, desc, completed);
    this.licensePlate = licensePlate;
    this.contact = contact;
    this.startDate = startDate;
    this.endDate = endDate;
    this.disability = disability;
  }

  public String getLicensePlate() {
    return licensePlate;
  }

  public int getContact() {
    return contact;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public boolean isDisability() {
    return disability;
  }
}
