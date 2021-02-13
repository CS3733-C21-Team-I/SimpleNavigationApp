package edu.wpi.ithorian.pathfinding;

public interface DistanceCalc<T extends GraphNode > {

	/**
	 * 
	 * @param from
	 * @param to
	 */
	double calculateDistance(T from, T to);

}