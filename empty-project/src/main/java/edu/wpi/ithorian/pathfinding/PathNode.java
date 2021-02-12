package edu.wpi.ithorian.pathfinding;

public class PathNode<T extends GraphNode> {

	private T node;
	private T previous;
	private double priority;

}