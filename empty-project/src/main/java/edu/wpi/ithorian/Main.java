package edu.wpi.ithorian;

import edu.wpi.ithorian.projectCTable.TableAppView;
import javafx.application.Application;
import edu.wpi.ithorian.database.NavDatabaseManager;
import edu.wpi.ithorian.hospitalMap.HospitalMap;
import java.util.Arrays;

public class Main {

  public static void main(String[] args) {

      if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
        NavDatabaseManager.init(true);
      } else {
        NavDatabaseManager.init(false);
      }

      HospitalMap hm = NavDatabaseManager.getInstance().loadMapFromMemory("Map1");
    String os = System.getProperty("os.name");
    System.out.println(os);
    String path =
        System.getProperty("user.dir")
            + "\\src\\main\\java\\edu\\wpi\\ithorian\\hospitalMap\\mapEditing\\";
    //    Set<HospitalMapNode> nodes =
    //        HospitalMap.generateElementFromData(
    //            ReadCSV.readFromFile(path + "MapINodes.csv"),
    //            ReadCSV.readFromFile(path + "MapIEdges.csv"));
    //    HospitalMap map =
    //        new HospitalMap("Test_Map", "Test Map", "Building1", 1, path + "FaulknerCampus.png",
    // nodes);
    Application.launch(TableAppView.class);
    // Application.launch(ApplicationView.class);
  }
}
