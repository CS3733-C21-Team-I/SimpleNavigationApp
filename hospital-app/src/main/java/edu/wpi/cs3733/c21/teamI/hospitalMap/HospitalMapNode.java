package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.pathfinding.GraphNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HospitalMapNode implements GraphNode<HospitalMapNode>, Cloneable {

  private String id;
  private String mapID;
  private List<HospitalMapNode> connections = new ArrayList<>();
  private int xCoord;
  private int yCoord;
  List<NodeRestrictions> nodeRestrictions = new ArrayList<>();

  public List<NodeRestrictions> getNodeRestrictions() {
    return nodeRestrictions;
  }

  public void setNodeRestrictions(List<NodeRestrictions> nodeRestrictions) {
    this.nodeRestrictions = nodeRestrictions;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setxCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public void setyCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  public int getxCoord() {
    return xCoord;
  }

  public int getyCoord() {
    return yCoord;
  }

  public HospitalMapNode() {}

  public HospitalMapNode(
      String id, String mapID, int xCoord, int yCoord, List<HospitalMapNode> connections) {
    this.id = id;
    this.mapID = mapID;
    this.connections = connections;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  public List<String> NodeAsList() {
    List<String> nodeElements = new ArrayList<String>();
    nodeElements.add(id);
    nodeElements.add(String.valueOf(this.xCoord));
    nodeElements.add(String.valueOf(this.yCoord));

    return nodeElements;
  }

  @Override
  public String getID() {
    return id;
  }

  @Override
  public List<HospitalMapNode> getConnections() {
    return connections;
  }

  @Override
  public void addConnection(HospitalMapNode add) {
    this.connections.add(add);
  }

  @Override
  public void removeConnection(HospitalMapNode take) {
    this.connections.remove(take);
  }

  public void setConnections(List<HospitalMapNode> connections) {
    this.connections = connections;
  }

  @Override
  public String toString() {
    return "HospitalMapNode{" + "id='" + id + ", xCoord=" + xCoord + ", yCoord=" + yCoord + '}';
  }

  public String getMapID() {
    return mapID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HospitalMapNode node = (HospitalMapNode) o;
    return id.equals(node.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);

  }
}
