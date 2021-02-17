package edu.wpi.ithorian;

import edu.wpi.ithorian.hospitalMap.tableEditing.FullHospitalMap;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullLocationNode;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullMapEditManager;
import edu.wpi.ithorian.projectCTable.TableAppView;
import java.util.Set;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    //    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
    //      NavDatabaseManager.init(true);
    //    } else {
    //      NavDatabaseManager.init(false);
    //    }
    //
    //    HospitalMap hm = NavDatabaseManager.getInstance().loadMapFromMemory("Map1");
    String path =
        System.getProperty("user.dir")
            + "/empty-project/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";
    Set<FullLocationNode> nodes =
        FullHospitalMap.generateElementFromData(
            ReadCSV.readFromFile(path + "MapINodes.csv"),
            ReadCSV.readFromFile(path + "MapIEdges.csv"));
    FullHospitalMap map =
        new FullHospitalMap(
            "Test_Map", "Test Map", "Building1", 1, path + "FaulknerCampus.png", nodes);
    FullMapEditManager mapManager = new FullMapEditManager();
    mapManager.init();
    mapManager.getInstance().setActiveMap(map);
    mapManager.getInstance().startTableView();
    Application.launch(TableAppView.class);
  }
}
