package edu.wpi.cs3733.c21.teamI.parking.reservations;

import java.sql.Date;

public class ParkingCustomer {
  String vehicleLicence;
  boolean isStaff;
  String contactNumber;
  Date registrationDate;

  public ParkingCustomer(
      String vehicleLicence, boolean isStaff, String contactNumber, Date registrationDate) {
    this.vehicleLicence = vehicleLicence;
    this.isStaff = isStaff;
    this.contactNumber = contactNumber;
    this.registrationDate = registrationDate;
  }

  public String getVehicleLicence() {
    return vehicleLicence;
  }

  public boolean isStaff() {
    return isStaff;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }
}
