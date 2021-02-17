package edu.wpi.ithorian.pathfinding;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.util.List;

public interface GraphNode<T extends GraphNode> {

  String getID();

  List<HospitalMapNode> getConnections();

  void addConnection(HospitalMapNode add);
}
