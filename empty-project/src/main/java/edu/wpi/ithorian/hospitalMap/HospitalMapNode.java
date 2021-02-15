package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.GraphNode;
import java.util.Set;

public class HospitalMapNode implements GraphNode<HospitalMapNode> {

  public void setId(String id) {
    this.id = id;
  }

  public void setxCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public void setyCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  private String id;
  private Set<HospitalMapNode> connections;

  public int getxCoord() {
    return xCoord;
  }

  public int getyCoord() {
    return yCoord;
  }

  private int xCoord;
  private int yCoord;
  public HospitalMapNode(){}

  public HospitalMapNode(String id, int xCoord, int yCoord, Set<HospitalMapNode> connections) {
    this.id = id;
    this.connections = connections;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  @Override
  public String getID() {
    return id;
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
