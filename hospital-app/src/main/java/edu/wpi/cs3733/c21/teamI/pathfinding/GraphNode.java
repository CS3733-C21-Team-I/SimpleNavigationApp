package edu.wpi.cs3733.c21.teamI.pathfinding;

import java.util.List;

public interface GraphNode<T extends GraphNode> {

  String getID();

  List<T> getConnections();

  void addConnection(T add);

  void removeConnection(T take);
}
