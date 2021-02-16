package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.GraphNode;

import java.util.ArrayList;
import java.util.List;


import java.util.Set;

public class HospitalMapNode implements GraphNode<HospitalMapNode> {



  private String id;
  private Set<HospitalMapNode> connections;

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

  private int xCoord;
  private int yCoord;

  public HospitalMapNode() {}

  public HospitalMapNode(String id, int xCoord, int yCoord, Set<HospitalMapNode> connections) {
    this.id = id;
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