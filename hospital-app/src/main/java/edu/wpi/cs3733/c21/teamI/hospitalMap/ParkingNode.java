package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.parking.ParkingNodeController;
import java.util.List;

public class ParkingNode extends LocationNode {
  private int parkingID;
  private boolean isOccupied;

  public ParkingNode(
      String id,
      String mapID,
      int xCoord,
      int yCoord,
      String shortName,
      String longName,
      LocationCategory category,
      String teamAssigned,
      List<HospitalMapNode> connections) {
    super(id, mapID, xCoord, yCoord, shortName, longName, LocationCategory.PARK, "I", connections);

    ParkingNodeController.getInstance().registerNode(this);
  }

  @Override
  public String toString() {
    return "ParkingNode: " + parkingID;
  }

  public boolean isOccupied() {
    System.out.println("isOccupied called");
    return true;
  }

  public void setOccupied(boolean occupied) {
    isOccupied = occupied;
  }

  public int getParkingID() {
    return parkingID;
  }

  public void setParkingID(int parkingID) {
    this.parkingID = parkingID;
  }
}
