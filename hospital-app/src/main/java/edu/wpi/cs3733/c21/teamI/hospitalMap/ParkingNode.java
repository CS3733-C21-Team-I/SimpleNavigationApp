package edu.wpi.cs3733.c21.teamI.hospitalMap;

import java.util.List;

public class ParkingNode extends LocationNode {

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
    super(id, mapID, xCoord, yCoord, shortName, longName, category, teamAssigned, connections);
  }

  public boolean isEmpty() {
    // TODO update from database when this is called
    return true;
  }
}
