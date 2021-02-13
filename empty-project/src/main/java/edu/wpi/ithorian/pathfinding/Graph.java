package edu.wpi.ithorian.pathfinding;

public interface Graph<T extends GraphNode> {

	/**
	 * 
	 * @param id
	 */
	T getNode(String id);

}