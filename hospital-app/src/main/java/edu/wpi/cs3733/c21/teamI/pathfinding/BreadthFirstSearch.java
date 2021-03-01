package edu.wpi.cs3733.c21.teamI.pathfinding;

import java.util.*;

public class BreadthFirstSearch implements PathPlanningAlgorithm {
  public <T extends GraphNode> List<T> findPath(T start, T end, PriorityCalc<T> scorer) {
    boolean foundLocation = false;
    HashMap<String, PathNode> visited = new HashMap<String, PathNode>();
    LinkedList<PathNode> queue = new LinkedList<>();

    PathNode<T> first = new PathNode<>(start, null, 0.0);

    visited.put(start.getID(), first);
    queue.add(first);
    PathNode current = first;
    T currNode = start;

    while (!queue.isEmpty()) {
      current = queue.poll();
      currNode = (T) current.getNode();
      List<T> connectedNodes = currNode.getConnections();

      // if node found, break from while loop
      if (currNode.equals(end)) {
        foundLocation = true;
        break;
      }

      // Searches all connected nodes
      for (T node : connectedNodes) {
        boolean shouldAvoid = !scorer.isValid(node);

        // Checks to see if node has been visited and is accessible based on scorer
        if (!visited.containsKey(node.getID()) && !shouldAvoid) {
          PathNode<T> pathNode = new PathNode<>(node, currNode, 0.0);
          visited.put(node.getID(), pathNode);
          queue.add(pathNode);
        }
      }
    }
    List<T> path = new ArrayList<>();

    if (foundLocation) {
      // Starting from the final node, follow the previous node back to the start
      while (current.getPrevious() != null) {
        path.add(currNode);

        current = visited.get(current.getPrevious().getID());
        currNode = (T) current.getNode();
      }
      path.add(currNode);

      // Reverses the path so that it begins from the start node
      Collections.reverse(path);
    } else {
      System.out.println("No Path Found :(");
    }

    // In the event where no path is found return an empty path
    return path;
  }
}
