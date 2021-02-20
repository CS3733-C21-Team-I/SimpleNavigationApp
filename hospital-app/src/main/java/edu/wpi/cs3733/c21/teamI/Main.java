package edu.wpi.cs3733.c21.teamI;

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
      NavDatabaseManager.init(true);
    } else {
      NavDatabaseManager.init(false);
    }

    String path =
        System.getProperty("user.dir")
            + "/src/main/java/edu/wpi/cs3733/c21/teamI/hospitalMap/mapEditing/";

    Map<String, HospitalMap> mapCollection =
        HospitalMapCSVBuilder.loadCSV(path + "MapINodes.csv", path + "MapIEdges.csv");

    MapEditManager mapManager = new MapEditManager();
    mapManager.init();
    mapManager.getInstance().setMapCollection(mapCollection);
    mapManager.getInstance().getDataCont().setActiveMap(mapCollection.get("Faulkner 0"));
    mapManager.getInstance().startApplicationView();
    Application.launch(ApplicationView.class);
  }
}
