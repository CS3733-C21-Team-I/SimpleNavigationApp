package edu.wpi.cs3733.c21.teamI.pathfinding;

public interface Graph<T extends GraphNode> {

  /** @param id */
  T getNode(String id);
}
