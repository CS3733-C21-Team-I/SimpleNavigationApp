package edu.wpi.ithorian.hospitalMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws IOException {

    //        if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
    //          NavDatabaseManager.init(true);
    //        } else {
    //          NavDatabaseManager.init(false);
    //        }
    //
    //        HospitalMap hm = NavDatabaseManager.getInstance().loadMapFromMemory("Map1");

    CSVOperator operator = new CSVOperator();

    List<List<String>> nodeList = new ArrayList<>();
    List<List<String>> edgeList = new ArrayList<>();
    ArrayList<LocationNode> locationNodes = new ArrayList<>();

    nodeList =
            operator.readCSV(
                    "empty-project/src/main/java/edu/wpi/ithorian/hospitalMap/MapINodesFixed.csv");
    edgeList =
            operator.readCSV("empty-project/src/main/java/edu/wpi/ithorian/hospitalMap/MapIEdges.csv");
    CSVOperator.generateElementFromData(nodeList, edgeList);
    locationNodes = CSVOperator.aNodes;

    for (int i = 0; i < locationNodes.size(); i++) {
      System.out.println(locationNodes.get(i));
    }
  }
}
