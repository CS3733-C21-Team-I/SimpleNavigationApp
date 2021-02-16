package edu.wpi.ithorian.hospitalMap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocationNode extends HospitalMapNode {

  private String shortName;
  private String longName;
  private String teamAssigned;
  private String building;
  private String nodeType;
  private int floor;
  public static List<LocationNode> connectedNodes = new ArrayList<>();

  public LocationNode() {};

  public LocationNode(List<String> nodeInit) {
    this.setId((String) nodeInit.get(0));
    this.setxCoord(Integer.parseInt((String) nodeInit.get(1)));
    this.setyCoord(Integer.parseInt((String) nodeInit.get(2)));
    this.floor = Integer.parseInt((String) nodeInit.get(3));
    this.building = (String) nodeInit.get(4);
    this.nodeType = (String) nodeInit.get(5);
    this.longName = (String) nodeInit.get(6);
    this.shortName = (String) nodeInit.get(7);
    this.teamAssigned = (String) nodeInit.get(8);
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

  @Override
  public List<String> NodeAsList() {
    List<String> nodeElements = super.NodeAsList();
    nodeElements.add(String.valueOf(this.shortName));
    nodeElements.add(this.longName);
    nodeElements.add("I");
    return nodeElements;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setTeamAssigned(String teamAssigned) {
    this.teamAssigned = teamAssigned;
  }
}
