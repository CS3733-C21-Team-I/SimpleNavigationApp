package edu.wpi.ithorian;

import edu.wpi.ithorian.database.NavDatabaseManager;
import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.ithorian.hospitalMap.mapEditing.ApplicationView;
import edu.wpi.ithorian.hospitalMap.mapEditing.MapEditManager;
import java.util.Arrays;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      NavDatabaseManager.init(true);
    } else {
      NavDatabaseManager.init(false);
    }

    String path =
        System.getProperty("user.dir") + "/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";

    HospitalMap map =
        HospitalMapCSVBuilder.loadCSV(path + "MapINodes.csv", path + "MapIEdges.csv")
            .get("Faulkner 0");

    MapEditManager mapManager = new MapEditManager();
    mapManager.init();
    mapManager.getInstance().setActiveMap(map);
    mapManager.getInstance().startApplicationView();
    Application.launch(ApplicationView.class);
  }
}
