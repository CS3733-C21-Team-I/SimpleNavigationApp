package edu.wpi.cs3733.c21.teamI.pathfinding;

import java.util.List;

public interface PathPlanningAlgorithm {
  <T extends GraphNode> List<T> findPath(T start, T end, PriorityCalc<T> scorer);
}
