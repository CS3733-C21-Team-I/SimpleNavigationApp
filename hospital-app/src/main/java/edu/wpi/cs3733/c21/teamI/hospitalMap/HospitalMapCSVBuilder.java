package edu.wpi.cs3733.c21.teamI.hospitalMap;

import java.io.*;
import java.util.*;

public class HospitalMapCSVBuilder {

  public static Map<String, HospitalMap> loadCSV(String nodePath, String edgePath) {

    Map<String, HospitalMap> maps = new HashMap<>();
    Map<String, HospitalMapNode> tempNodeMap = new HashMap<>();

    for (List<String> nodeEntry : readFromFile(nodePath)) {
      HospitalMapNode locationNode;
      if (nodeEntry.get(6).equals("LOC")) {
        locationNode =
            new LocationNode(
                nodeEntry.get(0),
                nodeEntry.get(10),
                Integer.parseInt(nodeEntry.get(1)),
                Integer.parseInt(nodeEntry.get(2)),
                nodeEntry.get(8),
                nodeEntry.get(7),
                LocationCategory.valueOf(nodeEntry.get(5)),
                nodeEntry.get(9),
                new ArrayList<>());
      } else {
        locationNode =
            new HospitalMapNode(
                nodeEntry.get(0),
                nodeEntry.get(10),
                Integer.parseInt(nodeEntry.get(1)),
                Integer.parseInt(nodeEntry.get(2)),
                new ArrayList<>());
      }
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
    class EdgePair {
      String fromId;
      String toId;

      public EdgePair(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgePair edgePair = (EdgePair) o;
        return (this.fromId.equals(edgePair.fromId) && this.toId.equals(edgePair.toId))
            || (this.fromId.equals(edgePair.toId) && this.toId.equals(edgePair.fromId));
      }

      @Override
      public int hashCode() {
        return Objects.hash(fromId) + Objects.hashCode(toId);
      }
    }

    StringBuilder nodesString = new StringBuilder();
    StringBuilder edgesString = new StringBuilder();

    Set<EdgePair> edgePairSet = new HashSet<>();

    nodesString.append(
        "nodeId,xcoord,ycoord,floor,building,nodetype,type,longname,shortname,teamassigned,mapId,,,OldY\n");

    for (HospitalMap map : maps) {
      String mapId = map.getId();
      String buildingName = map.getBuildingName();
      int floorNumber = map.getFloorNumber();

      for (HospitalMapNode node : map.getNodes()) {
        if (node instanceof LocationNode) {
          nodesString.append(node.getID() + ",");
          nodesString.append(node.getxCoord() + ",");
          nodesString.append(node.getyCoord() + ",");
          nodesString.append(floorNumber + ",");
          nodesString.append(buildingName + ",");
          nodesString.append(((LocationNode) node).getLocationCategory().toString() + ",");
          nodesString.append("LOC,");
          nodesString.append(((LocationNode) node).getLongName() + ",");
          nodesString.append(((LocationNode) node).getShortName() + ",");
          nodesString.append(((LocationNode) node).getTeamAssigned() + ",");
          nodesString.append(mapId + "\n");
        } else {
          System.out.println("Saving pos");
          nodesString.append(node.getID() + ",");
          nodesString.append(node.getxCoord() + ",");
          nodesString.append(node.getyCoord() + ",");
          nodesString.append(floorNumber + ",");
          nodesString.append(buildingName + ",");
          nodesString.append("Temp,");
          nodesString.append("POS,");
          nodesString.append(",");
          nodesString.append(",");
          nodesString.append(",");
          nodesString.append(mapId + "\n");
        }

        for (HospitalMapNode toNode : node.getConnections()) {
          edgePairSet.add(new EdgePair(node.getID(), toNode.getID()));
        }
      }
    }

    for (EdgePair edgePair : edgePairSet) {
      edgesString.append(edgePair.fromId + "_" + edgePair.toId + ",");
      edgesString.append(edgePair.fromId + ",");
      edgesString.append(edgePair.toId + "\n");
    }

    try {
      File nodes = new File(System.getProperty("user.dir") + "\\NewNodes.csv");
      try (FileWriter fr = new FileWriter(System.getProperty("user.dir") + "\\NewNodes.csv")) {
        fr.write(nodesString.toString());
      }
      File edges = new File(System.getProperty("user.dir") + "\\NewEdges.csv");
      try (FileWriter fr = new FileWriter(System.getProperty("user.dir") + "\\NewEdges.csv")) {
        fr.write(edgesString.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<List<String>> readFromFile(String path) {
    List<List<String>> allElements = new ArrayList<>();
    List<String> elementData;
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
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
      e.printStackTrace();
    }
    return allElements;
  }
}
