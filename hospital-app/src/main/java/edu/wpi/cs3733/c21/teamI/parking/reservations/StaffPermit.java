package edu.wpi.cs3733.c21.teamI.parking.reservations;

import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.Date;

public class StaffPermit {

  private int slotId;

  private User user;

  private Date assignedDate;

  private ParkingCustomer customer;

  public ParkingCustomer getCustomer() {
    return customer;
  }

  public StaffPermit(int slotId, User user, Date assignedDate, ParkingCustomer customer) {
    this.slotId = slotId;
    this.user = user;
    this.assignedDate = assignedDate;
    this.customer = customer;
  }

  public int getSlotId() {
    return slotId;
  }

  public User getUser() {
    return user;
  }

  public Date getAssignedDate() {
    return assignedDate;
  }
}
