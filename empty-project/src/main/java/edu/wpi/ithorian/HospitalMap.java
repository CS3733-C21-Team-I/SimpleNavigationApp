package edu.wpi.ithorian;

import edu.wpi.ithorian.Graph.Graph;
import edu.wpi.ithorian.Graph.GraphNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HospitalMap extends Graph<HospitalMap.Node> {
  // Lists that hold arrays of map nodes and edges based on csv input
  // static HashMap<String, Node> nodesHash = new HashMap<String, Node>();

  public HospitalMap() {
    nodes = new HashSet<>();
  }

  public HospitalMap(Set<Node> nodes) {
    this.nodes = nodes;
  }

  public void generateElementFromData(List<List<String>> nodesList, List<List<String>> edgesList) {
    for (List<String> values : nodesList) {
      Node currNode = new Node(values);
      nodes.add(currNode);
    }

    // iterates through edges and connects respective nodes
    for (List<String> values : edgesList) {
      Edge currEdge = new Edge(values);
      Node a = getNode(currEdge.startNode);
      Node b = getNode(currEdge.endNode);
      a.connectedNodes.add(b);
      b.connectedNodes.add(a);
    }
  }

  public static class Node implements GraphNode {
    public final String id, building, nodeType, longname, shortname, teamassigned;
    public List<Node> connectedNodes = new ArrayList<>();
    public final int xcoord, ycoord, floor;

    private Node(List nodeInit) {
      this.id = (String) nodeInit.get(0);
      this.xcoord = Integer.parseInt((String) nodeInit.get(1));
      this.ycoord = Integer.parseInt((String) nodeInit.get(2));
      this.floor = Integer.parseInt((String) nodeInit.get(3));
      this.building = (String) nodeInit.get(4);
      this.nodeType = (String) nodeInit.get(5);
      this.longname = (String) nodeInit.get(6);
      this.shortname = (String) nodeInit.get(7);
      this.teamassigned = (String) nodeInit.get(8);
    }

    public Node(
        String id,
        String building,
        String nodeType,
        String longname,
        String shortname,
        String teamassigned,
        int xcoord,
        int ycoord,
        int floor,
        List<Node> connections) {
      this.id = id;
      this.building = building;
      this.nodeType = nodeType;
      this.longname = longname;
      this.shortname = shortname;
      this.teamassigned = teamassigned;
      this.xcoord = xcoord;
      this.ycoord = ycoord;
      this.floor = floor;
      this.connectedNodes = connections;
    }

    public int getXcoord() {
      return xcoord;
    }

    public int getYcoord() {
      return ycoord;
    }

    public String getID() {
      return this.id;
    }

    public List<Node> getConnections() {
      return this.connectedNodes;
    }

    public void setConnections(List<Node> connectedNodes) {
      this.connectedNodes = connectedNodes;
    }

    @Override
    public String toString() {
      return "MapNode '" + id + '\'';
    }

    public String longToString() {
      return "MapNode{"
          + "id='"
          + id
          + '\''
          + ", building='"
          + building
          + '\''
          + ", nodeType='"
          + nodeType
          + '\''
          + ", longname='"
          + longname
          + '\''
          + ", shortname='"
          + shortname
          + '\''
          + ", teamassigned='"
          + teamassigned
          + '\''
          + ", xcoord="
          + xcoord
          + ", ycoord="
          + ycoord
          + ", floor="
          + floor
          + '}';
    }
  }

  public static class Edge {
    public final String id, startNode, endNode;

    private Edge(List nodeInit) {
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
  }
}
