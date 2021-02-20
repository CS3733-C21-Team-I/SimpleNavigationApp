package edu.wpi.cs3733.c21.teamI.pathfinding;

public class PathNode<T extends GraphNode> {

  private final T node;
  private T previous;
  private double priority;

  PathNode(T current) {
    this(current, null, Double.POSITIVE_INFINITY);
  }

  PathNode(T current, T previous, double score) {
    this.node = current;
    this.previous = previous;
    this.priority = score;
  }

  public T getNode() {
    return node;
  }

  public T getPrevious() {
    return previous;
  }

  public double getPriority() {
    return priority;
  }
}
