package edu.wpi.ithorian.pathfinding;

import java.util.List;

public interface GraphNode<T extends GraphNode> {

	String getID();

	List<T> getConnections();

}