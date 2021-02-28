package edu.wpi.cs3733.c21.teamI.pathfinding;

import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import java.util.ArrayList;

public class TextDirections {

  public static String[] getDirections(EuclidianDistCalc calc, ArrayList<HospitalMapNode> path) {
    String[] directions = new String[0];
    // TODO
    return directions;
  }

  public String describeStep(
      EuclidianDistCalc calc, HospitalMapNode last, HospitalMapNode curr, HospitalMapNode next) {
    String step = "";
    return step;
  }

  // only worth describing if there's multiple possible decisions
  public boolean isJunction(HospitalMapNode node) {
    return node.getConnections().size() > 2;
  }

  // returns angle in degrees; negative if on the right hand side, positive if on the left
  public double angleDegrees(
      EuclidianDistCalc calc, HospitalMapNode a, HospitalMapNode vertex, HospitalMapNode b) {
    // law of cosines
    double aLen = calc.calculateDistance(vertex, b);
    double bLen = calc.calculateDistance(vertex, a);
    double vertLen = calc.calculateDistance(a, b);

    double lawOfCosines =
        (Math.pow(aLen, 2) + Math.pow(bLen, 2) - Math.pow(vertLen, 2)) / (2 * aLen * bLen);
    lawOfCosines = Math.toDegrees(Math.acos(lawOfCosines));

    // determine if on the left side of the line
    double crossProduct =
        (vertex.getxCoord() - a.getxCoord()) * (b.getyCoord() - a.getyCoord())
            - (vertex.getyCoord() - a.getyCoord()) * (b.getxCoord() - a.getxCoord());
    boolean isLeft = crossProduct > 0;
    if (isLeft) {
      lawOfCosines = lawOfCosines * -1;
    }

    return lawOfCosines;
  }
}
