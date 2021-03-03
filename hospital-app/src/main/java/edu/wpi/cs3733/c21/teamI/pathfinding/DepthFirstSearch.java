package edu.wpi.cs3733.c21.teamI.pathfinding;

import java.util.*;

public class DepthFirstSearch implements PathPlanningAlgorithm {
  public <T extends GraphNode> List<T> findPath(T start, T end, PriorityCalc<T> scorer) {
    Stack<T> frontier = new Stack<>();
    Stack<ArrayList<T>> pathStack = new Stack<>();
    Set<T> visited = new HashSet<>();

    // Initialize frontier with start node
    frontier.push(start);
    ArrayList<T> pathInit = new ArrayList<>();
    pathInit.add(start);
    pathStack.push(pathInit);

    while (!frontier.empty()) {
      ArrayList<T> currPath = pathStack.pop();
      T currNode = frontier.pop();
      visited.add(currNode);
      List<T> connectedNodes = currNode.getConnections();

      for (T node : connectedNodes) {
        boolean shouldAvoid = !scorer.isValid(node);
        if (!visited.contains(node) && !shouldAvoid) {
          ArrayList<T> tempPath = new ArrayList(currPath);
          tempPath.add(node);
          if (node.equals(end)) {
            return tempPath;
          } else {
            frontier.push(node);
            pathStack.push(tempPath);
          }
        }
      }
    }
    // In the event where no path is found return an empty path
    return new ArrayList<>();
  }
}
