package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.Graph;

import java.util.List;
import java.util.Set;

public class HospitalMap implements Graph<HospitalMapNode> {

  private Set<HospitalMapNode> nodes;
  private String id;
  private String mapName;
  private String buildingName;
  private String imagePath;
  private int floorNumber;

  public HospitalMap(
      String id,
      String mapName,
      String buildingName,
      int floorNumber,
      String imagePath,
      Set<HospitalMapNode> nodes) {
    this.nodes = nodes;
    this.id = id;
    this.mapName = mapName;
    this.buildingName = buildingName;
    this.floorNumber = floorNumber;
    this.imagePath = imagePath;
  }

  @Override
  public HospitalMapNode getNode(String id) {
    return nodes.stream()
        .filter(n -> n.getID().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No node for given id:" + id));
  }

  public Set<HospitalMapNode> getNodes() {
    return nodes;
  }


  public static class Edge {
    public final String id, startNode, endNode;

    public Edge(List nodeInit) {
      this.id = (String) nodeInit.get(0);
      this.startNode = (String) nodeInit.get(1);
      this.endNode = (String) nodeInit.get(2);
    }

    @Override
    public String toString() {
      return "MapEdge{"
          + "startNode='"
          + startNode
          + '\''
          + ", endNode='"
          + endNode
          + '\''
          + ", id="
          + id
          + '}';
    }

  public String getId() {
    return id;
  }

  public String getMapName() {
    return mapName;
  }

  public String getBuildingName() {
    return buildingName;
  }

  public String getImagePath() {
    return imagePath;
  }

  public int getFloorNumber() {
    return floorNumber;

  }
}
