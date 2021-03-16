package edu.wpi.cs3733.c21.teamI.util;

import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import java.util.regex.Pattern;

public class InputChecking {

  // returns true if a username does not already exist
  public static boolean validNewUsername(String newUsername) {

    boolean withinRegEx =
        Pattern.compile("^(?=[a-zA-Z0-9._]{1,30}$)(?!.*[_.]{2})[^_.].*[^_.]$")
            .matcher(newUsername)
            .matches();
    if (withinRegEx
        && UserDatabaseManager.getInstance().getCurrentUsernames().contains(newUsername)) {
      return false;
    }
    return true;
  }
  // returns true if there exists a node that has the inputted string as a longName in the database
  public static boolean validLocationName(String longName) {
    if (longName.length() > 0) {
      for (HospitalMapNode node : MapDataEntity.getNodesSet()) {
        if (node instanceof LocationNode && ((LocationNode) node).getLongName().equals(longName)) {
          return true;
        }
      }
    }
    return false;
  }
}
