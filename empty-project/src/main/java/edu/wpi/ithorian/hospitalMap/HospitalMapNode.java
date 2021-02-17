package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.GraphNode;
import java.util.ArrayList;
import java.util.List;

public class HospitalMapNode implements GraphNode<HospitalMapNode> {

  private String id;
  private String mapID;
  private List<HospitalMapNode> connections = new ArrayList<>();
  private int xCoord;
  private int yCoord;

  public void setxCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public void setyCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  public void setId(String id) {
    this.id = id;
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

  public void setConnections(List<HospitalMapNode> connections) {
    this.connections = connections;
  }

  @Override
  public String toString() {
    return "HospitalMapNode{" + "id='" + id + ", xCoord=" + xCoord + ", yCoord=" + yCoord + '}';
  }
}
