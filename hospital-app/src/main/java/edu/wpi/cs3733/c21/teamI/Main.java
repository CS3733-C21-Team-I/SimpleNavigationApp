package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.DatabaseManager;
import edu.wpi.cs3733.c21.teamI.view.HomeController;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    ApplicationDataController.init();

    //    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
    DatabaseManager.initDatabaseManagers(true);
    DatabaseManager.regenTables();
    //    } else {
    //      NavDatabaseManager.init(false);
    //    }

    Application.launch(HomeController.class);
  }
}
