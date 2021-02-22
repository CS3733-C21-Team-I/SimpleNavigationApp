package edu.wpi.cs3733.c21.teamI.hospitalMap;

import java.util.ArrayList;

public class LocationNode extends HospitalMapNode {

  private String shortName;
  private String longName;
  private String teamAssigned;
  private String building;
  private LocationCategory locationCategory;
  private int floor;
  public static List<LocationNode> connectedNodes = new ArrayList<>();

  public LocationNode() {}

  public LocationNode(List<String> nodeInit) {
    this.setId((String) nodeInit.get(0));
    this.setxCoord(Integer.parseInt((String) nodeInit.get(1)));
    this.setyCoord(Integer.parseInt((String) nodeInit.get(2)));
    this.floor = Integer.parseInt((String) nodeInit.get(3));
    this.building = (String) nodeInit.get(4);
    //    this.locationCategory =
    //        (LocationCategory)
    //            nodeInit.get(
    //                5); // This was previously nodeType but is being renamed to LocationCategory
    // which
    // is an enum
    this.longName = (String) nodeInit.get(6);
    this.shortName = (String) nodeInit.get(7);
    this.teamAssigned = (String) nodeInit.get(8);
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

  public String getShortName() {
    return shortName;
  }

  public String getLongName() {
    return longName;
  }

  public String getTeamAssigned() {
    return teamAssigned;
  }

  @Override
  public String toString() {
    return "Node: " + longName + " (a.k.a) " + shortName + " for team: " + teamAssigned;
  }
}
