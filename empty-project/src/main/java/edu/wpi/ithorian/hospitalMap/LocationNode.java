package edu.wpi.ithorian.hospitalMap;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationNode extends HospitalMapNode implements Serializable {

  private String shortName;
  private String longName;
  private String teamAssigned;

  public LocationNode() {}

  public LocationNode(
      String id,
      String mapID,
      int xCoord,
      int yCoord,
      String shortName,
      String longName,
      String teamAssigned,
      ArrayList<HospitalMapNode> connections) {
    super(id, mapID, xCoord, yCoord, connections);
    this.shortName = shortName;
    this.longName = longName;
    this.teamAssigned = teamAssigned;
  }

  @Override
  public String toString() {
    return "Node: " + longName + " (a.k.a) " + shortName + " for team: " + teamAssigned;
  }
}
