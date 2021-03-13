package edu.wpi.cs3733.c21.teamI.hospitalMap;

public class ParkingNode extends HospitalMapNode {
  private String parkingID;

  @Override
  public String toString() {
    return "ParkingNode: " + parkingID;
  }

  public boolean isEmpty() {
    // TODO update from database when this is called
    return true;
  }
}
