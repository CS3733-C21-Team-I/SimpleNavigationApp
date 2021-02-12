package edu.wpi.ithorian.hospitalMap;

import edu.wpi.ithorian.pathfinding.GraphNode;

import java.util.List;
import java.util.Set;

public class HospitalMapNode implements GraphNode<HospitalMapNode> {

	private String id;
	private Set<HospitalMapNode> connections;
	private int xCoord;
	private int yCoord;

	public HospitalMapNode(String id, int xCoord, int yCoord, Set<HospitalMapNode> connections) {
		this.id = id;
		this.connections = connections;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public List<HospitalMapNode> getConnections() {
		return null;
	}

	public void setConnections(Set<HospitalMapNode> connections) {
		this.connections = connections;
	}
}