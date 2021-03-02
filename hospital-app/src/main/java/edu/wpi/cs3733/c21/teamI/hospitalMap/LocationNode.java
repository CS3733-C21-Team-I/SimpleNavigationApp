package edu.wpi.cs3733.c21.teamI.hospitalMap;

import java.util.ArrayList;
import java.util.List;

public class LocationNode extends HospitalMapNode {

  private String shortName;
  private String longName;
  private String teamAssigned;
  private String building;
  private LocationCategory locationCategory;
  private int floor;
  public static List<LocationNode> connectedNodes = new ArrayList<>();

  public LocationNode() {}

  public LocationNode(
      String id,
      String mapID,
      int xCoord,
      int yCoord,
      String shortName,
      String longName,
      LocationCategory category,
      String teamAssigned,
      List<HospitalMapNode> connections) {
    super(id, mapID, xCoord, yCoord, connections);
    this.shortName = shortName;
    this.longName = longName;
    this.teamAssigned = teamAssigned;
    this.locationCategory = category;
  }

  public String getShortName() {
    return shortName;
  }

  public String getLongName() {
    return longName;
  }

  public String getTeamAssigned() {
    return teamAssigned;
  }

  public LocationCategory getLocationCategory() {
    return this.locationCategory;
  }

  @Override
  public String toString() {
    return "Node: " + longName + " (a.k.a) " + shortName + " for team: " + teamAssigned;
  }

  public void setShortName(String sName) {
    this.shortName = sName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }
}
