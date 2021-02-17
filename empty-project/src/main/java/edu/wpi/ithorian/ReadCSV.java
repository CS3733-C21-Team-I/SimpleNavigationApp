package edu.wpi.ithorian;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullHospitalMap;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullLocationNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ReadCSV {

  // HERE AS A TEMPORARY MEASURE
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

  public static void writeCSVFull(FullHospitalMap hMap) throws IOException {

    List<List<String>> listOfNodes = new ArrayList<>();

    for (FullLocationNode node : hMap.getNodes()) {
      listOfNodes.add(node.getNodeString());
    }

    FileWriter nodeFileWriter = new FileWriter("node.csv");

    nodeFileWriter.append("nodeID");
    nodeFileWriter.append(",");
    nodeFileWriter.append("xcoord");
    nodeFileWriter.append(",");
    nodeFileWriter.append("ycoord");
    nodeFileWriter.append(",");
    nodeFileWriter.append("floor");
    nodeFileWriter.append(",");
    nodeFileWriter.append("building");
    nodeFileWriter.append(",");
    nodeFileWriter.append("nodeType");
    nodeFileWriter.append(",");
    nodeFileWriter.append("longName");
    nodeFileWriter.append(",");
    nodeFileWriter.append("shortName");
    nodeFileWriter.append(",");
    nodeFileWriter.append("teamAssigned");
    nodeFileWriter.append("\n");

    for (List<String> singleRow : listOfNodes) {
      nodeFileWriter.append(String.join(",", singleRow));
      nodeFileWriter.append("\n");
    }
    nodeFileWriter.flush();
    nodeFileWriter.close();

    class EdgePair {
      String toId;
      String edgeId;
      String fromId;

      public EdgePair(String fromId, String toId) {
        this.edgeId = fromId + "_" + toId;
        this.fromId = fromId;
        this.toId = toId;
      }

      public boolean equals(EdgePair other) {
        return (this.fromId.equals(other.fromId) && this.toId.equals(other.toId))
            || (this.fromId.equals(other.toId) && this.toId.equals(other.fromId));
      }

      public List<String> edgeAsList() {
        List<String> edge = new ArrayList<>();
        edge.add(this.edgeId);
        edge.add(this.fromId);
        edge.add(this.toId);

        return edge;
      }
    }
    Set<List<String>> ListOfAllEdges = new HashSet<>();
    for (FullLocationNode node : hMap.getNodes()) {
      for (HospitalMapNode toNode : node.getConnections()) {
        ListOfAllEdges.add(new EdgePair(node.getID(), toNode.getID()).edgeAsList());
      }
    }

    FileWriter edgeFileWriter = new FileWriter("edge.csv");

    edgeFileWriter.append("id");
    edgeFileWriter.append(",");
    edgeFileWriter.append("startNode");
    edgeFileWriter.append(",");
    edgeFileWriter.append("endNode");
    edgeFileWriter.append("\n");

    for (List<String> singleRow : ListOfAllEdges) {
      edgeFileWriter.append(String.join(",", singleRow));
      edgeFileWriter.append("\n");
    }
    edgeFileWriter.flush();
    edgeFileWriter.close();
  }
}
