package edu.wpi.ithorian;

import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.hospitalMap.mapEditing.CSVOperator;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

  public static void main(String[] args) throws IOException {

    //    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
    //      NavDatabaseManager.init(true);
    //    } else {
    //      NavDatabaseManager.init(false);
    //    }
    //
    //    HospitalMap hm = NavDatabaseManager.getInstance().loadMapFromMemory("Map1");

    CSVOperator csvOperator = new CSVOperator();

    Set<HospitalMapNode> connectionsForNode1 = new HashSet<>();
    HospitalMapNode node1 = new HospitalMapNode("id1", 1, 1, connectionsForNode1);

    Set<HospitalMapNode> connectionsForNode2 = new HashSet();
    HospitalMapNode node2 = new HospitalMapNode("id2", 2, 2, connectionsForNode2);

    Set<HospitalMapNode> connectionsForNode3 = new HashSet<>();
    HospitalMapNode node3 = new HospitalMapNode("id3", 3, 3, connectionsForNode3);

    Set<HospitalMapNode> connectionsForNode4 = new HashSet<>();
    HospitalMapNode node4 = new HospitalMapNode("id4", 4, 4, connectionsForNode4);

    connectionsForNode1.add(node2);
    connectionsForNode1.add(node3);
    connectionsForNode1.add(node4);
    connectionsForNode2.add(node1);
    connectionsForNode2.add(node3);
    connectionsForNode2.add(node4);
    connectionsForNode3.add(node2);
    connectionsForNode3.add(node1);
    connectionsForNode3.add(node4);

    Set<HospitalMapNode> nodes = new HashSet<>();
    nodes.add(node1);
    nodes.add(node2);
    nodes.add(node3);
    nodes.add(node4);

    HospitalMap map1 = new HospitalMap("mapId", "mapName", "buildingName", 1, "imagePath", nodes);

    csvOperator.writeCSV(map1);



  }
}
