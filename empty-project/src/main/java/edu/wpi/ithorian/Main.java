package edu.wpi.ithorian;

import edu.wpi.ithorian.database.NavDatabaseManager;
import java.util.Arrays;

public class Main {

  public static void main(String[] args) {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      NavDatabaseManager.init(true);
    } else {
      NavDatabaseManager.init(false);
    }

    // App.launch(App.class, args);
  }
}
