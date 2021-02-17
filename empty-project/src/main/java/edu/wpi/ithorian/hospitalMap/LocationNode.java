package edu.wpi.ithorian.hospitalMap;

import java.util.Set;

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
      int xCoord,
      int yCoord,
      String shortName,
      String longName,
      String teamAssigned,
      Set<HospitalMapNode> connections) {
    super(id, xCoord, yCoord, connections);
    this.shortName = shortName;
    this.longName = longName;
    this.teamAssigned = teamAssigned;
  }
}
