package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.DatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.view.HomeController;
import edu.wpi.cs3733.c21.teamI.view.MapEditManager;
import javafx.application.Application;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) {

    ApplicationDataController.init();

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      DatabaseManager.initDatabaseManagers(true);
      DatabaseManager.regenTables();
    } else {
      NavDatabaseManager.init(false);
    }

    MapEditManager.init();
    MapEditManager.getInstance().startApplicationView();
    Application.launch(HomeController.class);
  }
}
