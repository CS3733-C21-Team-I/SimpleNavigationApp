package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.PriorityCalc;
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
    // TODO: when enumeration exists, exclude nodeTypesToAvoid
    return true;

  }
}
