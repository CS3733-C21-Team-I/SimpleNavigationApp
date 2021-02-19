package edu.wpi.ithorian.hospitalMap;

import java.util.ArrayList;

public class LocationNode extends HospitalMapNode {

  private String shortName;
  private String longName;
  private String teamAssigned;

  public String getShortName() {
    return shortName;
  }

  public String getLongName() {
    return longName;
  }

  public String getTeamAssigned() {
    return teamAssigned;
  }

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
}
