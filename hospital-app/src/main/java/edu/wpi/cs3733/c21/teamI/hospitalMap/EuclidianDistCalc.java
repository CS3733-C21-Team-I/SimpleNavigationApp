package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.pathfinding.PriorityCalc;
import java.util.List;

public class EuclidianDistCalc implements PriorityCalc<HospitalMapNode> {

  public List<String> nodeTypesToAvoid;

  public double calculateDistance(HospitalMapNode from, HospitalMapNode to) {
    /**
     * @param from
     * @param to
     * @return
     */
    double distance, xDiff, yDiff = 0;
    xDiff = Math.abs(from.getxCoord() - to.getxCoord());
    yDiff = Math.abs(from.getyCoord() - to.getyCoord());

    distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

    return distance;
  }

  @Override
  public boolean isValid(HospitalMapNode node) {
    boolean canAccess = true;

    // TODO: when enumeration exists, exclude nodeTypesToAvoid
    //    for (String type: nodeTypesToAvoid){
    //      if (node.nodeType = type){
    //        canAccess = false;
    //        break;
    //      }
    //    }

    return canAccess;
  }
}
