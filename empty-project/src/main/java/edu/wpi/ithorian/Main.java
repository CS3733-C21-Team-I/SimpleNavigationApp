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

    HospitalMap hm = NavDatabaseManager.getInstance().loadMapFromMemory("Map1");

    for (HospitalMap.Node n : hm.nodes) {
      System.out.println(n.longToString());
      System.out.println("\tConnections:");
      for (HospitalMap.Node c : n.connectedNodes) {
        System.out.println("\t\t" + c);
      }
    }
    // App.launch(App.class, args);
  }
}
