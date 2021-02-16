package edu.wpi.ithorian.pathfinding;

import java.util.List;
import java.util.Set;

public interface GraphNode<T extends GraphNode> {

	String getID();

	Set<T> getConnections();

}