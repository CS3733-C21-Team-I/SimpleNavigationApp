package edu.wpi.ithorian.pathfinding;

import java.util.Comparator;

public class PathNodeComparator implements Comparator<PathNode> {

  @Override
  public int compare(PathNode a, PathNode b) {
    if (a.getPriority() == b.getPriority()) {
      return 0;
    } else if (a.getPriority() < b.getPriority()) {
      return -1;
    } else {
      return 1;
    }
  }

  public int equals(PathNode o1) {
    return 0;
  }
}
