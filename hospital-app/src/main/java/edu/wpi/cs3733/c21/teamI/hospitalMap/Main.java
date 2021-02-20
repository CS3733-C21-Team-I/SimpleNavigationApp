package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
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