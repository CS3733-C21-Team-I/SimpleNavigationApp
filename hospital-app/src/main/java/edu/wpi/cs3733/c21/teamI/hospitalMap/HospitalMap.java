package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.pathfinding.Graph;
import java.util.*;

public class HospitalMap implements Graph<HospitalMapNode> {

  private final Set<HospitalMapNode> nodes;
  private final String id;
  private final String mapName;
  private final String buildingName;
  private final String imagePath;
  private final int floorNumber;

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

  public HospitalMap(HospitalMap toClone) {
    Map<String, HospitalMapNode> newNodes = new HashMap<>();
    Map<String, List<String>> newConnections = new HashMap<>();

    for (HospitalMapNode nodeToClone : toClone.nodes) {
      if (nodeToClone instanceof HospitalMapNode)
        newNodes.put(
            nodeToClone.getID(),
            new HospitalMapNode(
                nodeToClone.getID(),
                nodeToClone.getMapID(),
                nodeToClone.getxCoord(),
                nodeToClone.getyCoord(),
                new ArrayList<>()));
      else if (nodeToClone instanceof LocationNode)
        newNodes.put(nodeToClone.getID(), new LocationNode());
      else
        throw new IllegalStateException(
            "Found unhandled node type in HospitalMap copy constructor");

      List<String> cloneNodeConnections = new ArrayList<>();
      for (HospitalMapNode cloneConnection : nodeToClone.getConnections()) {
        cloneNodeConnections.add(cloneConnection.getID());
      }
      newConnections.put(nodeToClone.getID(), cloneNodeConnections);
    }

    for (String newNodeId : newConnections.keySet()) {
      for (String newConnectedNodeId : newConnections.get(newNodeId)) {
        if (newNodes.containsKey(newConnectedNodeId)) {
          newNodes.get(newNodeId).addConnection(newNodes.get(newConnectedNodeId));
        } else {
          newNodes
              .get(newNodeId)
              .addConnection(
                  toClone.getNode(newNodeId).getConnections().stream()
                      .filter(n -> n.getID().equals(newConnectedNodeId))
                      .findFirst()
                      .orElseThrow(
                          () ->
                              new IllegalArgumentException(
                                  "Attemplted to link to node never linked to in cloned HospitalMap")));
        }
      }
    }

    this.nodes = new HashSet<>(newNodes.values());
    this.id = toClone.id;
    this.mapName = toClone.mapName;
    this.buildingName = toClone.buildingName;
    this.floorNumber = toClone.floorNumber;
    this.imagePath = toClone.imagePath;
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

  public void removeNode(String nodeId) {
    nodes.remove(getNode(nodeId));
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append("HospitalMap: " + id + " containing: " + "\n");
    for (HospitalMapNode node : nodes) {
      out.append("\t" + node.getID() + "\n");
    }
    return out.toString();
  }
}
