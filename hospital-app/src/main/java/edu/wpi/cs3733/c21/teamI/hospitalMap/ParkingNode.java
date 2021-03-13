package edu.wpi.cs3733.c21.teamI.hospitalMap;

import java.util.List;

public class ParkingNode extends HospitalMapNode {
  private String parkingID;

  public ParkingNode(
      String id,
      String mapID,
      int xCoord,
      int yCoord,
      String longName,
      List<HospitalMapNode> connections) {
    super(id, mapID, xCoord, yCoord, connections);
    this.parkingID = longName;
  }

  @Override
  public String toString() {
    return "ParkingNode: " + parkingID;
  }

  public boolean isEmpty() {
    // TODO update from database when this is called
    return true;
  }
}
