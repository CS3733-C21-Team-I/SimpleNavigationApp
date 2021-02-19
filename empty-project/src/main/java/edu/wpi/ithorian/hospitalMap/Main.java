package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.database.NavDatabaseManager;
import java.io.IOException;
import java.util.Arrays;

public class Main {

  public static void main(String[] args) throws IOException {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      NavDatabaseManager.init(true);
    } else {
      NavDatabaseManager.init(false);
    }
  }
}
