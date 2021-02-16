package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.GraphNode;
import java.util.Set;

public class HospitalMapNode implements GraphNode<HospitalMapNode> {

  private String id;
  private int xCoord;
  private int yCoord;
  private String buildingName;

  private String nodeType;
  private int floor;
  private Set<HospitalMapNode> connections;

  public HospitalMapNode() {}

  public HospitalMapNode(String id, int xCoord, int yCoord, Set<HospitalMapNode> connections) {
    this.id = id;
    this.connections = connections;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  public HospitalMapNode(
      String id,
      int xCoord,
      int yCoord,
      String buildingName,
      String nodeType,
      int floor,
      Set<HospitalMapNode> connections) {
    this.id = id;
    this.connections = connections;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.buildingName = buildingName;
    this.nodeType = nodeType;
    this.floor = floor;
  }

  @Override
  public String getID() {
    return id;
  }

  public int getxCoord() {
    return xCoord;
  }

  public int getyCoord() {
    return yCoord;
  }

  public String getBuildingName() {
    return buildingName;
  }

  public String getNodeType() {
    return nodeType;
  }

  public int getFloor() {
    return floor;
  }

  @Override
  public Set<HospitalMapNode> getConnections() {
    return connections;
  }

  public void setConnections(Set<HospitalMapNode> connections) {
    this.connections = connections;
  }

  @Override
  public String toString() {
    return "HospitalMapNode{" + "id='" + id + ", xCoord=" + xCoord + ", yCoord=" + yCoord + '}';
  }
}
