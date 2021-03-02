package edu.wpi.cs3733.c21.teamI.pathfinding;

import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationCategory;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.util.ArrayList;
import java.util.List;

public class TextDirections {

  public static ArrayList<String> getDirections(
      EuclidianDistCalc calc, List<HospitalMapNode> path) {
    ArrayList<String> directions = new ArrayList<>();

    if (path.size() < 2) {
      directions.add("No path was found.");
      return directions;
    }

    // describe start location
    String startDirection = "Begin ";
    HospitalMapNode first = path.get(0);
    if (first instanceof LocationNode) {
      startDirection += "at " + ((LocationNode) first).getLongName() + ", ";
    }
    startDirection += "facing " + compassDirection(first, path.get(1)) + ".";
    directions.add(startDirection);

    for (int i = 1; i < (path.size() - 1); i++) {

      String step = "";

      if (isFloorChange(path.get(i), path.get(i + 1))) {
        if (path.get(i) instanceof LocationNode) {
          step = describeFloorchangeStep(((LocationNode) path.get(i)), path.get(i + 1)) + ".";
        } else {
          step = "Proceed to floor " + path.get(i + 1).getMapID() + ".";
        }
        directions.add(step);

      } else {
        step = describeStep(calc, path.get(i - 1), path.get(i), path.get(i + 1));
        if (path.get(i) instanceof LocationNode) {
          step += " at " + ((LocationNode) path.get(i)).getLongName();
        }
        step += ".";

        if (!isRepeat(path, directions, i, step) && worthDescription(path.get(i))) {
          directions.add(step);
        }
      }
    }

    // describe end location
    HospitalMapNode last = path.get(path.size() - 1);
    if (last instanceof LocationNode) {
      directions.add("Continue until you reach " + ((LocationNode) last).getLongName() + ".");
    }

    return directions;
  }

  public static boolean isFloorChange(HospitalMapNode curr, HospitalMapNode next) {
    return !(curr.getMapID().equals(next.getMapID()));
  }

  public static String describeStep(
      EuclidianDistCalc calc, HospitalMapNode last, HospitalMapNode curr, HospitalMapNode next) {
    String step = "";

    double turn = angleDegrees(calc, last, curr, next);
    if (turn > 165 || turn < -165) {
      step += "Continue straight";
    } else {
      step += "Take a ";
      if (!(Math.abs(turn) < 120)) {
        step += "slight ";
      }
      if ((Math.abs(turn) < 60)) {
        step += "sharp ";
      }
      if (turn < 0) {
        step += "right";
      } else {
        step += "left";
      }
    }
    return step;
  }

  public static boolean worthDescription(HospitalMapNode node) {
    return node.getConnections().size() > 2;
  };

  public static String describeFloorchangeStep(LocationNode curr, HospitalMapNode next) {
    String step = "";
    LocationCategory type = curr.getLocationCategory();
    if (type != null) {
      switch (type) {
        case ELEV:
          step = "Take the elevator to floor " + next.getMapID();
          break;
        case STAI:
          step = "Take the stairs to floor " + next.getMapID();
          break;
        case EXIT:
          step = "Enter the door to " + next.getMapID();
          break;
        default:
          break;
      }
    }
    return step;
  }

  public static boolean isRepeat(
      List<HospitalMapNode> path, List<String> directions, int i, String instruction) {
    // if shortname is the same as the last location node, like elevators nd stuff
    if (path.get(i) instanceof LocationNode && path.get(i - 1) instanceof LocationNode) {
      if (((LocationNode) path.get(i))
          .getShortName()
          .equals(((LocationNode) path.get(i - 1)).getShortName())) {
        return true;
      }
    }
    if (instruction.equals("Continue straight.")
        && directions.get(directions.size() - 1).equals("Continue straight.")) {
      return true;
    }
    return false;
  }

  public static String compassDirection(HospitalMapNode start, HospitalMapNode facing) {
    String direction = "";
    double deltaY = facing.getyCoord() - start.getyCoord();
    double deltaX = facing.getxCoord() - start.getxCoord();

    if (deltaX == 0) { // preventing divide by zero error
      if (deltaY > 0) return "north";
      else return "south";
    }
    // get angle assuming east is 0 degrees
    double facingAngle = Math.toDegrees(Math.atan2(deltaY, deltaX));
    System.out.println(facingAngle);

    if (Math.abs(facingAngle) > 25 && Math.abs(facingAngle) < 155) {
      if (facingAngle > 0) direction += "south";
      else direction += "north";
    }
    if (facingAngle > -65 && facingAngle < 65) direction += "east";
    if (facingAngle < -115 || facingAngle > 115) direction += "west";

    return direction;
  }

  // returns angle of turn represented by 3 locations in degrees; negative if on the right hand
  // side, positive if on the left
  public static double angleDegrees(
      EuclidianDistCalc calc, HospitalMapNode a, HospitalMapNode vertex, HospitalMapNode b) {
    // law of cosines
    double aLen = calc.calculateDistance(vertex, b);
    double bLen = calc.calculateDistance(vertex, a);
    double vertLen = calc.calculateDistance(a, b);

    double lawOfCosines =
        (Math.pow(aLen, 2) + Math.pow(bLen, 2) - Math.pow(vertLen, 2)) / (2 * aLen * bLen);
    lawOfCosines = Math.toDegrees(Math.acos(lawOfCosines));

    // determine if on the right side of the line
    double crossProduct =
        (vertex.getxCoord() - a.getxCoord()) * (b.getyCoord() - a.getyCoord())
            - (vertex.getyCoord() - a.getyCoord()) * (b.getxCoord() - a.getxCoord());
    boolean isRight = crossProduct > 0;
    if (isRight) {
      lawOfCosines = lawOfCosines * -1;
    }

    return lawOfCosines;
  }
}
