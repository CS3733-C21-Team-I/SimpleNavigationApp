package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;

import java.util.LinkedList;
import java.util.Queue;

public class MapEditDataController {

	private static MapEditDataController ourInstance;

	public static void init() {
		ourInstance = new MapEditDataController();
	}

	public static MapEditDataController getInstance() {
		return ourInstance;
	}

	/** FIFO Queue of operations for passing to the databaseManager */
	private Queue<NavEditOperation> dataOperations;

	/**
	 * Represents the map that is activelyBeing edited should be referenced from this class's getter
	 * for all UI calls
	 */
	private HospitalMap activeMap;

	private MapEditDataController() {
		activeMap = null;
		dataOperations = new LinkedList<>();
	}

	/**
	 * Called to add a entirely new node to the HospitalMap
	 *
	 * @param node the node to be added, currently requires assigning a uniqueNode ID
	 */
	public void addNode(HospitalMapNode node) {
		// TODO set node id using hashing?
		dataOperations.add(
				new NavEditOperation(NavEditOperation.OperationType.ADD_NODE, null, node, null));
		activeMap.getNodes().add(node);
	}

	/**
	 * Called when editing a node. Should be passed a new instance of HospitalMapNode representing the
	 * edited node Usage, editNode(nodeId, new HospitalMapNode(newInfo)
	 *
	 * @param nodeId the Id of the node prior to editing
	 * @param newNode
	 */
	public void editNode(String nodeId, LocationNode newNode) {
		HospitalMapNode node = activeMap.getNode(nodeId);
		// TODO reset node.id using hashing?
		dataOperations.add(
				new NavEditOperation(NavEditOperation.OperationType.EDIT_NODE, nodeId, node, null));
	}

	/**
	 * Called when deleting a node
	 *
	 * @param nodeId the id of the node to be removed
	 */
	public void deleteNode(String nodeId) {
		dataOperations.add(
				new NavEditOperation(NavEditOperation.OperationType.DELETE_NODE, nodeId, null, null));
		activeMap.getNodes().remove(activeMap.getNode(nodeId));
	}

	public void addEdge(String fromNode, String toNode) {
		dataOperations.add(
				new NavEditOperation(NavEditOperation.OperationType.ADD_NODE, fromNode, null, toNode));
		activeMap.getNode(fromNode).getConnections().add(activeMap.getNode(toNode));
		activeMap.getNode(toNode).getConnections().add(activeMap.getNode(fromNode));
	}

	public void deleteEdge(String fromNode, String toNode) {
		dataOperations.add(
				new NavEditOperation(NavEditOperation.OperationType.ADD_NODE, fromNode, null, toNode));
		activeMap.getNode(fromNode).getConnections().add(activeMap.getNode(toNode));
		activeMap.getNode(toNode).getConnections().add(activeMap.getNode(fromNode));
	}

	public HospitalMap getActiveMap() { return activeMap; }

	/**
	 * Sets the map to be edited, should be called upon entering the map edit UI state with a
	 * HospitalMap loaded from NavDatabaseManager
	 *
	 * @param activeMap The instance of map being edited.
	 */
	public void setActiveMap(HospitalMap activeMap) {
		this.activeMap = activeMap;
	}

//	+ saveChanges()
	public void saveChanges() {
		// TODO send to Database
		activeMap = null;
	}

//	+ discardChanges()

// 	+ getClosestNodeToPos(int xPos, int yPos): HospitalMapNode

//	+ undo()
//	+ redo()
}
