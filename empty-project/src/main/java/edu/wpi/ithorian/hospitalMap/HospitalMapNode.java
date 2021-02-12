package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.GraphNode;

import java.util.List;
import java.util.Set;

public class HospitalMapNode implements GraphNode<HospitalMapNode> {

	private String id;
	private Set<HospitalMapNode> connections;
	private int xCoord;
	private int yCoord;

	@Override
	public String getID() {
		return null;
	}

	@Override
	public List<HospitalMapNode> getConnections() {
		return null;
	}
}