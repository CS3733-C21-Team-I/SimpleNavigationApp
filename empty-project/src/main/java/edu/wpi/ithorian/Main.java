package edu.wpi.ithorian;

import edu.wpi.ithorian.database.NavDatabaseManager;
import edu.wpi.ithorian.projectCTable.TableAppView;
import java.util.Arrays;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      NavDatabaseManager.init(true);
    } else {
      NavDatabaseManager.init(false);
    }

    Application.launch(TableAppView.class);
    // Application.launch(ApplicationView.class);
  }
}
