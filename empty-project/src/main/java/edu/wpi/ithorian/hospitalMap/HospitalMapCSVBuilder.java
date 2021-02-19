package edu.wpi.ithorian.hospitalMap;

import java.io.*;
import java.util.*;

public class HospitalMapCSVBuilder {

  public static Map<String, HospitalMap> loadCSV(String nodePath, String edgePath) {

    Map<String, HospitalMap> maps = new HashMap<>();
    Map<String, HospitalMapNode> tempNodeMap = new HashMap<>();

    for (List<String> nodeEntry : readFromFile(nodePath)) {
      LocationNode locationNode =
          new LocationNode(
              nodeEntry.get(0),
              nodeEntry.get(10),
              Integer.parseInt(nodeEntry.get(1)),
              Integer.parseInt(nodeEntry.get(2)),
              nodeEntry.get(8),
              nodeEntry.get(7),
              nodeEntry.get(9),
              new ArrayList<>());
      if (!maps.containsKey(nodeEntry.get(10))) {
        maps.put(
            nodeEntry.get(10),
            new HospitalMap(
                nodeEntry.get(10),
                "Temp",
                nodeEntry.get(4),
                Integer.parseInt(nodeEntry.get(3)),
                "TEMP",
                new HashSet<>()));
      }
      maps.get(nodeEntry.get(10)).getNodes().add(locationNode);
      tempNodeMap.put(nodeEntry.get(0), locationNode);
    }

    for (List<String> edgeEntry : readFromFile(edgePath)) {
      HospitalMapNode a = tempNodeMap.get(edgeEntry.get(1));
      HospitalMapNode b = tempNodeMap.get(edgeEntry.get(2));
      a.getConnections().add(b);
      b.getConnections().add(a);
    }

    return maps;
  }

  public static void saveCSV(Collection<HospitalMap> maps, String nodePath, String edgePath) {
    class EdgePair implements Comparable<EdgePair> {
      String fromId;
      String toId;

      public EdgePair(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
      }

      public int compareTo(EdgePair other) {
        boolean out =
            (this.fromId.equals(other.fromId) && this.toId.equals(other.toId))
                || (this.fromId.equals(other.toId) && this.toId.equals(other.fromId));
        if (out) {
          return 0;
        } else if (this.hashCode() > other.hashCode()) {
          return 1;
        } else {
          return -1;
        }
      }
    }

    StringBuilder nodesString = new StringBuilder();
    StringBuilder edgesString = new StringBuilder();

    Set<EdgePair> edgePairSet = new TreeSet<>();

    for (HospitalMap map : maps) {
      String mapId = map.getId();
      String buildingName = map.getBuildingName();
      int floorNumber = map.getFloorNumber();

      for (HospitalMapNode node : map.getNodes()) {
        if (node instanceof LocationNode) {
          nodesString.append(node.getID() + ", ");
          nodesString.append(node.getxCoord() + ", ");
          nodesString.append(node.getyCoord() + ". ");
          nodesString.append(floorNumber + ", ");
          nodesString.append(buildingName + ", ");
          nodesString.append("Temp, ");
          nodesString.append("LOC, ");
          nodesString.append(((LocationNode) node).getLongName() + ", ");
          nodesString.append(((LocationNode) node).getShortName() + ", ");
          nodesString.append(((LocationNode) node).getTeamAssigned() + ", ");
          nodesString.append(mapId);
        } else {
          nodesString.append(node.getID() + ", ");
          nodesString.append(node.getxCoord() + ", ");
          nodesString.append(node.getyCoord() + ". ");
          nodesString.append(floorNumber + ", ");
          nodesString.append(buildingName + ", ");
          nodesString.append("Temp, ");
          nodesString.append("LOC, ");
          nodesString.append(", ");
          nodesString.append(", ");
          nodesString.append(", ");
          nodesString.append(mapId + "\n");
        }

        for (HospitalMapNode toNode : node.getConnections()) {
          edgePairSet.add(new EdgePair(node.getID(), toNode.getID()));
        }
      }
    }

    for (EdgePair edgePair : edgePairSet) {
      edgesString.append(edgePair.fromId + "_" + edgePair.toId + ", ");
      edgesString.append(edgePair.fromId + ", ");
      edgesString.append(edgePair.toId + "\n");
    }

    try {
      FileWriter nodeFile = new FileWriter(nodePath);
      nodeFile.write(nodesString.toString());
      FileWriter edgeFile = new FileWriter(edgePath);
      edgeFile.write(edgesString.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<List<String>> readFromFile(String path) {
    List<List<String>> allElements = new ArrayList<>();
    List<String> elementData;
    try {
      System.out.println(HospitalMapCSVBuilder.class.getResource(path));
      BufferedReader br =
          new BufferedReader(
              new InputStreamReader(HospitalMapCSVBuilder.class.getResourceAsStream(path)));
      String line;
      boolean skip = true;
      while ((line = br.readLine()) != null) {
        if (skip) {
          skip = false;
        } else {
          String[] values = line.split(",");
          elementData = Arrays.asList(values);
          allElements.add(elementData);
        }
      }
    } catch (IOException e) {
      System.out.println(new File(path).getAbsolutePath());
      e.printStackTrace();
    }
    return allElements;
  }
}
