package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathPlanningAlgorithm;
import java.util.ArrayList;
import java.util.List;

public class MapDataEntity {

  public EuclidianDistCalc scorer = new EuclidianDistCalc();
  public PathPlanningAlgorithm pathFinderAlgorithm = new PathFinder();
  private List<HospitalMapNode> foundPath;

  // pathfinding functions
  public void clearFoundPath() {
    foundPath = null;
  }

  public boolean foundPathExists() {
    return foundPath != null && !foundPath.isEmpty();
  }

  public List<HospitalMapNode> getFoundPath() {
    if (foundPathExists()) {
      return foundPath;
    } else {
      return new ArrayList<HospitalMapNode>();
    }
  }

  public List<HospitalMapNode> getFoundPath(HospitalMapNode nodeA, HospitalMapNode nodeB) {
    if (foundPathExists()
        && foundPath.get(0).equals(nodeA)
        && foundPath.get(foundPath.size() - 1).equals(nodeB)) {
      System.out.println("Path already existed:" + this.foundPath);
      return foundPath;
    } else {
      foundPath = pathFinderAlgorithm.findPath(nodeA, nodeB, scorer);
      // ArrayList<String> directions = TextDirections.getDirections(scorer, foundPath);
      return foundPath;
    }
  }
}
