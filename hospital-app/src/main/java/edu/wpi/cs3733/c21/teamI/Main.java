package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.DatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.ApplicationView;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditManager;
import java.util.Arrays;
import java.util.Map;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      DatabaseManager.initDatabaseManagers(true);
      DatabaseManager.regenTables();

      Map<String, HospitalMap> maps =
          HospitalMapCSVBuilder.loadCSV("MapINodes.csv", "MapIEdges.csv");
      NavDatabaseManager.getInstance().saveMapsIntoMemory(maps.values());
    } else {
      NavDatabaseManager.init(false);
    }

    HospitalMap map = NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0");

    MapEditManager mapManager = new MapEditManager();
    mapManager.init();
    mapManager
        .getInstance()
        .setMapCollection(NavDatabaseManager.getInstance().loadMapsFromMemory());
    mapManager
        .getInstance()
        .getDataCont()
        .setActiveMap(NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0"));
    mapManager.getInstance().startApplicationView();
    Application.launch(ApplicationView.class);
  }
}
