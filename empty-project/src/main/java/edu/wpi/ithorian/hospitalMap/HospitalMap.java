package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.Graph;

import java.util.Set;

public class HospitalMap implements Graph {

	private Set<hospitalMap.HospitalMapNode> nodes;
	private String id;
	private String mapName;
	private String buildingName;
	private int floorNumber;

	@Override
	public pathfinding.GraphNode getNode(String id) {
		return null;
	}
}