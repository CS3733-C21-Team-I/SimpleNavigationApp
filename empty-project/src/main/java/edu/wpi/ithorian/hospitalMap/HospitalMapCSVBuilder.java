package edu.wpi.ithorian.hospitalMap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HospitalMapCSVBuilder {


//  /** @param path */
//  public HospitalMap loadCSV(String path) {
//    // TODO - implement HospitalMapCSVBuilder.loadCSV
//    throw new UnsupportedOperationException();
//  }
//}

    public static void writeFile(String nodeCsvFilePath, String edgeCsvFilePath, HospitalMap hMap) throws IOException {
//
        // List nodes= new ArrayList<>(hMap.getNodes());
        List<List<String>> listOfNodes = new ArrayList<>();

        for (HospitalMapNode node : hMap.getNodes()) {

            listOfNodes.add(node.NodeAsList());
        }

        FileWriter nodeFileWriter = new FileWriter(nodeCsvFilePath, true);

        nodeFileWriter.append("id");
        nodeFileWriter.append(",");
        nodeFileWriter.append("xcoord");
        nodeFileWriter.append(",");
        nodeFileWriter.append("ycoord");
        nodeFileWriter.append("shortName");
        nodeFileWriter.append("longName");
        nodeFileWriter.append("I");
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
                return (this.fromId.equals(other.fromId) && this.toId.equals(other.toId)) ||
                        (this.fromId.equals(other.toId) && this.toId.equals(other.fromId));
            }
        }
        Set<EdgePair> edgePairSet = new HashSet<>();
        for (HospitalMapNode node : hMap.getNodes()) {
            for (HospitalMapNode toNode : node.getConnections()) {
                edgePairSet.add(new EdgePair(node.getID(), toNode.getID()));
            }
        }

        List edges = new ArrayList<>(edgePairSet);

        List<List<String>> edgeRowElements = new ArrayList<>();


        FileWriter edgeFileWriter = new FileWriter(edgeCsvFilePath, true);

        edgeFileWriter.append("id");
        edgeFileWriter.append(",");
        edgeFileWriter.append("startNode");
        edgeFileWriter.append(",");
        edgeFileWriter.append("endNode");

//
//
        for (List<String> singleRow : edgeRowElements) {
            edgeFileWriter.append(String.join(",", singleRow));
            edgeFileWriter.append("\n");
        }
    }


}

//
