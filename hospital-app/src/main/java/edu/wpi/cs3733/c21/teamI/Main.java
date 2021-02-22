package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.DatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.ApplicationView;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditManager;
import java.util.Arrays;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    ApplicationDataController.init();

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      DatabaseManager.initDatabaseManagers(true);
      DatabaseManager.regenTables();
    } else {
      NavDatabaseManager.init(false);
    }

    HospitalMap map = NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0");

    MapEditManager.init();
    MapEditManager.getInstance()
        .setMapCollection(NavDatabaseManager.getInstance().loadMapsFromMemory());
    MapEditManager.getInstance()
        .getDataCont()
        .setActiveMap(NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0"));
    MapEditManager.getInstance().startApplicationView();
    Application.launch(ApplicationView.class);
  }
}
