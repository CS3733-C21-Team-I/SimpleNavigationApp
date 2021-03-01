package edu.wpi.cs3733.c21.teamI.pathfinding;

import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.util.ArrayList;
import java.util.List;

public class TextDirections {

  public static ArrayList<String> getDirections(
      EuclidianDistCalc calc, List<HospitalMapNode> path) {
    ArrayList<String> directions = new ArrayList<String>();

    if (path.size() < 2) {
      directions.add("You are already at your destination.");
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

    int distance = 0;
    for (int i = 1; i < (path.size() - 1); i++) {
      if (worthDescription(path.get(i))) {
        String step = describeStep(calc, path.get(i - 1), path.get(i), path.get(i + 1));
        if (distance > 0) step = "In " + distance + "m, " + step;
        directions.add(step);
        distance = 0;
      } else {
        distance += calcDistance(calc, path.get(i - 1), path.get(i));
      }
    }
    // describe end location
    HospitalMapNode last = path.get(path.size() - 1);
    if (last instanceof LocationNode) {
      directions.add("Continue until you reach " + ((LocationNode) last).getLongName() + ".");
    }

    return directions;
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

    if (curr instanceof LocationNode) {
      step += " at " + ((LocationNode) curr).getLongName();
    }

    step += ".";

    return step;
  }

  // only worth describing if there's multiple possible decisions at that point OR it's a named
  // location
  public static boolean worthDescription(HospitalMapNode node) {
    return node.getConnections().size() > 2;
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
