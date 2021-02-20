package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.ApplicationView;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditManager;
import java.util.Arrays;
import java.util.Map;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      NavDatabaseManager.init(true);

      Map<String, HospitalMap> maps =
          HospitalMapCSVBuilder.loadCSV("MapINodes.csv", "MapIEdges.csv");
      NavDatabaseManager.getInstance().saveMapsIntoMemory(maps.values());
    } else {
      NavDatabaseManager.init(false);
    }

    HospitalMap map = NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0");

    for (HospitalMapNode node : map.getNodes()) {
      System.out.println(node.getID());
    }

    MapEditManager mapManager = new MapEditManager();
    mapManager.init();
    mapManager.getInstance().setActiveMap(map);
    mapManager.getInstance().startApplicationView();
    Application.launch(ApplicationView.class);
  }
}
