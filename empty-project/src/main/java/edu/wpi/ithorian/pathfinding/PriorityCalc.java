package edu.wpi.ithorian.pathfinding;

public interface PriorityCalc<T extends GraphNode> {

  /**
   * @param from
   * @param to
   */
  double calculateDistance(T from, T to);

  boolean isValid(T node);
}
