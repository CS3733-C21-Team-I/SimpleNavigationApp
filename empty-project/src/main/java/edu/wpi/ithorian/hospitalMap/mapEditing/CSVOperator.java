package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.hospitalMap.LocationNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSVOperator {

  public static ArrayList<LocationNode> aNodes= new ArrayList<>();

  public static   void generateElementFromData(

      List<List<String>> nodesList, List<List<String>> edgesList) {

    Set<LocationNode> nodes= new HashSet<>();



    HashMap<String, LocationNode> nodesHash =new HashMap<>();
    for (List<String> values : nodesList) {

      LocationNode currNode = new LocationNode(values);
//      aNodes.add(currNode);
      aNodes.add(currNode);
      nodesHash.put(currNode.getID(), currNode);
    }

    // iterates through edges and connects respective nodes
    for (List<String> values : edgesList) {
      HospitalMap.Edge currEdge = new HospitalMap.Edge(values);
      LocationNode a = nodesHash.get(currEdge.startNode);
      LocationNode b = nodesHash.get(currEdge.endNode);
      a.connectedNodes.add(b);
      b.connectedNodes.add(a);
    }



  }



    public static List<List<String>> readCSV(String path) {
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


  public static void writeCSV(HospitalMap hMap) throws IOException {
    //
    // List nodes= new ArrayList<>(hMap.getNodes());
    List<List<String>> listOfNodes = new ArrayList<>();

    for (HospitalMapNode node : hMap.getNodes()) {

      listOfNodes.add(node.NodeAsList());
    }

    FileWriter nodeFileWriter = new FileWriter("node.csv");

    //        nodeFileWriter.append("id");
    //        nodeFileWriter.append(",");
    //        nodeFileWriter.append("xcoord");
    //        nodeFileWriter.append(",");
    //        nodeFileWriter.append("ycoord");
    //        nodeFileWriter.append("shortName");
    //        nodeFileWriter.append("longName");
    //        nodeFileWriter.append("I");
    //        nodeFileWriter.append("\n");

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
    for (HospitalMapNode node : hMap.getNodes()) {
      for (HospitalMapNode toNode : node.getConnections()) {
        ListOfAllEdges.add(new EdgePair(node.getID(), toNode.getID()).edgeAsList());
      }
    }

    FileWriter edgeFileWriter = new FileWriter("edge.csv");

    //        edgeFileWriter.append("id");
    //        edgeFileWriter.append(",");
    //        edgeFileWriter.append("startNode");
    //        edgeFileWriter.append(",");
    //        edgeFileWriter.append("endNode");

    //
    //
    for (List<String> singleRow : ListOfAllEdges) {
      edgeFileWriter.append(String.join(",", singleRow));
      edgeFileWriter.append("\n");
    }
    edgeFileWriter.flush();
    edgeFileWriter.close();
  }
}
