package edu.wpi.ithorian.Graph;

import java.util.List;

public interface GraphNode<T extends GraphNode<T>> {
  String getID();

  int getXcoord();

  int getYcoord();

  List<T> getConnections();
}
