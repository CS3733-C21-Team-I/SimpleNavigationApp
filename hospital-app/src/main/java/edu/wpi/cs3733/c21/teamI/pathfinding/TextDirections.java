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

  String describeStep(EuclidianDistCalc calc, HospitalMapNode last, HospitalMapNode curr, HospitalMapNode next) {
    String step = "";
    return step;
  }

  double angle(EuclidianDistCalc calc, HospitalMapNode a, HospitalMapNode vertex, HospitalMapNode b) {
    // law of cosines
    double aLen = calc.calculateDistance(vertex, b);
    double bLen = calc.calculateDistance(vertex, a);
    double vertLen = calc.calculateDistance(a, b);

    double lawOfCosines = (Math.pow(aLen,2) + Math.pow(bLen,2) - Math.pow(vertLen,2))/(2*aLen*bLen);
    return 0;
  }
}
