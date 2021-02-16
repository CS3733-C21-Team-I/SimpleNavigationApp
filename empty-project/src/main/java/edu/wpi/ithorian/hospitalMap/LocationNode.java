package edu.wpi.ithorian.hospitalMap;

import java.io.Serializable;
import java.util.List;

public class LocationNode extends HospitalMapNode implements Serializable {

  private String shortName;
  private String longName;
  private String teamAssigned;

  public LocationNode() {}

  public LocationNode(
      String id,
      int xCoord,
      int yCoord,
      String shortName,
      String longName,
      String teamAssigned,
      List<HospitalMapNode> connections) {
    super(id, xCoord, yCoord, connections);
    this.shortName = shortName;
    this.longName = longName;
    this.teamAssigned = teamAssigned;
  }

  public LocationNode(
      String id,
      String buildingName,
      String nodeType,
      String longname,
      String shortname,
      String teamAssigned,
      int xCoord,
      int yCoord,
      int floor,
      List<HospitalMapNode> connections) {
    super(id, xCoord, yCoord, buildingName, nodeType, floor, connections);
    this.shortName = shortName;
    this.longName = longName;
    this.teamAssigned = teamAssigned;
  }

  @Override
  public String toString() {
    return "Node: " + longName + " (a.k.a) " + shortName + " for team: " + teamAssigned;
  }
}
