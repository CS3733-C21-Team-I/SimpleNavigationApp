package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.pathfinding.Graph;
import java.util.*;

public class HospitalMap implements Graph<HospitalMapNode> {

  private Set<HospitalMapNode> nodes;
  private String id;
  private String mapName;
  private String buildingName;
  private String imagePath;
  private int floorNumber;

  private HospitalMapNode selectedNode = null;

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

  public static Set<HospitalMapNode> generateElementFromData(
      List<List<String>> nodesList, List<List<String>> edgesList) {
    HashMap<String, HospitalMapNode> nodesHash = new HashMap<>();
    HashSet<HospitalMapNode> nodes = new HashSet<>();
    for (List<String> value : nodesList) {
      HospitalMapNode currNode =
          new HospitalMapNode(
              value.get(0),
              "",
              Integer.parseInt(value.get(1)),
              Integer.parseInt(value.get(2)),
              new ArrayList<>());
      nodes.add(currNode);
      nodesHash.put(currNode.getID(), currNode);
    }

    // iterates through edges and connects respective nodes
    for (List<String> values : edgesList) {
      HospitalMapNode a = nodesHash.get(values.get(1));
      HospitalMapNode b = nodesHash.get(values.get(2));
      a.addConnection(b);
      b.addConnection(a);
    }

    return nodes;
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

  public HospitalMapNode getSelectedNode() {
    return selectedNode;
  }

  public void setSelectedNode(HospitalMapNode selectedNode) {
    this.selectedNode = selectedNode;
  }

  public void removeNode(String nodeId) {
    nodes.remove(getNode(nodeId));
  }
}