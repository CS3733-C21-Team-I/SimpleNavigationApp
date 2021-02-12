package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.Graph;
import edu.wpi.ithorian.pathfinding.GraphNode;

import java.util.Set;

public class HospitalMap implements Graph<HospitalMapNode> {

	private Set<HospitalMapNode> nodes;
	private String id;
	private String mapName;
	private String buildingName;
	private int floorNumber;

	public HospitalMap(String id, String mapName, String buildingName, int floorNumber, Set<HospitalMapNode> nodes) {
		this.nodes = nodes;
		this.id = id;
		this.mapName = mapName;
		this.buildingName = buildingName;
		this.floorNumber = floorNumber;
	}

	@Override
	public HospitalMapNode getNode(String id) {
		return nodes.stream().filter(n -> n.getID().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("No node for given id:" + id));
	}
}