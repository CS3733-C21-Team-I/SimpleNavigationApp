package edu.wpi.ithorian;

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
  }
}
