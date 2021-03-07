package edu.wpi.cs3733.c21.teamI.pathfinding;

import java.util.List;

public class AlgorithmSelectionStrategyPattern<T extends GraphNode> {
  private PathPlanningAlgorithm planning;

  public AlgorithmSelectionStrategyPattern(PathPlanningAlgorithm planning) {
    this.planning = planning;
  }

  public List<T> findPath(T start, T end, PriorityCalc<T> scorer) {
    return planning.findPath(start, end, scorer);
  }
}
