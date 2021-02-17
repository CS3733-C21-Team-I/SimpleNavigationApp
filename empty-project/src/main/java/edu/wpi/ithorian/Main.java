package edu.wpi.ithorian;

import edu.wpi.ithorian.projectCTable.TableAppView;
import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.hospitalMap.mapEditing.MapEditManager;
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
    String os = System.getProperty("os.name");
    System.out.println(os);
    String path =
        System.getProperty("user.dir")
            + "/empty-project/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";
    Set<HospitalMapNode> nodes =
        HospitalMap.generateElementFromData(
            ReadCSV.readFromFile(path + "MapINodes.csv"),
            ReadCSV.readFromFile(path + "MapIEdges.csv"));
    HospitalMap map =
        new HospitalMap("Test_Map", "Test Map", "Building1", 1, path + "FaulknerCampus.png", nodes);
    MapEditManager mapManager = new MapEditManager();
    mapManager.init();
    mapManager.getInstance().setActiveMap(map);
    mapManager.getInstance().startTableView();
    Application.launch(TableAppView.class);
  }
}
