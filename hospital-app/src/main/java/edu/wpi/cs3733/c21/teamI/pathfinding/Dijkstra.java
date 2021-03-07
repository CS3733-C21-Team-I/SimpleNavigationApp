package edu.wpi.cs3733.c21.teamI.pathfinding;

public class Dijkstra extends AstarDijkstraTemplatePattern {

  public <T extends GraphNode> double heuristic(T next, T end, PriorityCalc<T> scorer) {
    return 0;
  }
}
