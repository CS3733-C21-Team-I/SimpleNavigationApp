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

    NavDatabaseManager.init((args.length > 0) && Arrays.asList(args).contains("regenerate"));

    Map<String, HospitalMap> mapCollection =
        HospitalMapCSVBuilder.loadCSV(
            Main.class.getResource("/csv/MapINodes.csv").getPath(),
            Main.class.getResource("/csv/MapIEdges.csv").getPath());

    MapEditManager.init();
    MapEditManager.getInstance().setMapCollection(mapCollection);
    MapEditManager.getInstance().getDataCont().setActiveMap(mapCollection.get("Faulkner 0"));
    MapEditManager.getInstance().startApplicationView();
    Application.launch(ApplicationView.class);
  }
}
