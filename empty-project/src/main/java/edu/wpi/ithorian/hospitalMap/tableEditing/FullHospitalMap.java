package edu.wpi.ithorian.hospitalMap.tableEditing;

import edu.wpi.ithorian.pathfinding.Graph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullHospitalMap implements Graph<FullLocationNode> {

  private Set<FullLocationNode> nodes;
  private String id;
  private String mapName;
  private String buildingName;
  private String imagePath;
  private int floorNumber;

  public FullHospitalMap(
      String id,
      String mapName,
      String buildingName,
      int floorNumber,
      String imagePath,
      Set<FullLocationNode> nodes) {
    this.nodes = nodes;
    this.id = id;
    this.mapName = mapName;
    this.buildingName = buildingName;
    this.floorNumber = floorNumber;
    this.imagePath = imagePath;
  }

  // this will be deleted before merging to main, just can't find the read from csv function so
  // improvising for now
  public static Set<FullLocationNode> generateElementFromData(
      List<List<String>> nodesList, List<List<String>> edgesList) {
    HashMap<String, FullLocationNode> nodesHash = new HashMap<String, FullLocationNode>();
    HashSet<FullLocationNode> nodes = new HashSet<>();
    for (List<String> value : nodesList) {
      FullLocationNode currNode =
          new FullLocationNode(
              value.get(0),
              Integer.parseInt(value.get(1)),
              Integer.parseInt(value.get(2)),
              value.get(3),
              value.get(4),
              value.get(5),
              value.get(6),
              value.get(7),
              value.get(8),
              new HashSet());
      nodes.add(currNode);
      nodesHash.put(currNode.getID(), currNode);
    }

    // iterates through edges and connects respective nodes
    for (List<String> values : edgesList) {
      FullLocationNode a = nodesHash.get(values.get(1));
      FullLocationNode b = nodesHash.get(values.get(2));
      a.addConnection(b);
      b.addConnection(a);
    }

    return nodes;
  }

  @Override
  public FullLocationNode getNode(String id) {
    return nodes.stream()
        .filter(n -> n.getID().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No node for given id:" + id));
  }

  public Set<FullLocationNode> getNodes() {
    return nodes;
  }

  public String getId() {
    return id;
  }
}
