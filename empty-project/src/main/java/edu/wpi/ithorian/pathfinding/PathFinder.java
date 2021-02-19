package edu.wpi.ithorian.pathfinding;

import java.util.*;

public class PathFinder<T extends GraphNode> {

  public static <T extends GraphNode> List<T> findPath(T start, T end, PriorityCalc<T> scorer) {

    Boolean foundLocation = false;
    PriorityQueue<PathNode> frontier = new PriorityQueue<>(new PathNodeComparator());
    // all visited nodes-- stored in a Pathnode with the node, the last node visited, and the cost
    // to it
    // access the pathnode by the ID of the current node
    HashMap<String, PathNode> visited = new HashMap<String, PathNode>();
    // start from first node
    PathNode<T> first = new PathNode<>(start, null, 0.0);
    frontier.add(first);
    visited.put(start.getID(), first);

    double priority = 0.0;

    PathNode current = first;
    T currentNode = start;

    while (!frontier.isEmpty()) {
      current = frontier.poll();
      currentNode = (T) current.getNode();
      // if node found
      if (currentNode.equals(end)) {
        // System.out.println("Found!");
        foundLocation = true;
        break;
      }

      List<T> connectedNodes = currentNode.getConnections();
      for (T next : connectedNodes) {
        Boolean shouldAvoid = !scorer.isValid(next);

        double newCost = current.getPriority() + scorer.calculateDistance(currentNode, next);
        if ((!visited.containsKey(next.getID())
                || newCost < visited.get(next.getID()).getPriority())
            && !shouldAvoid) {
          // System.out.println("Visiting node" + next.getID()  + "   " + newCost);
          // make the PathNode that stores where it came from & its cost; add it to visited nodes
          PathNode<T> newPath = new PathNode<T>(next, currentNode, newCost);
          visited.put(next.getID(), newPath);

          priority = newCost + scorer.calculateDistance(end, next);
          // System.out.println("priority: " + priority);
          frontier.add(new PathNode<>(next, currentNode, priority));
        }
      }
    }

    List<T> path = new ArrayList<>();

    if (foundLocation) {
      // reverse through cameFrom to get the properly ordered path from start to end
      while (current.getPrevious() != null) {
        // System.out.println("Adding " + currentNode.getID() + " to path...");
        path.add(currentNode);

        current = visited.get(current.getPrevious().getID());
        currentNode = (T) current.getNode();
      }
      path.add(currentNode);
      Collections.reverse(path);
    } else {
      System.out.println("No Path Found :(");
    }

    return path;
  }
}
