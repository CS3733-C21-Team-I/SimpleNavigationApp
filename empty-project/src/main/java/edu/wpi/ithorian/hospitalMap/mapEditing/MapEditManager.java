package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;


import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static edu.wpi.ithorian.hospitalMap.mapEditing.NavEditOperation.OperationType.*;

public class MapEditManager {

    private static MapEditManager ourInstance;

    public static void init() {
        ourInstance = new MapEditManager();
    }

    public static MapEditManager getInstance() {
        return ourInstance;
    }

    /**
     * Represents the map that is activelyBeing edited should be referenced from this class's getter for all UI calls
     */
    private HospitalMap activeMap;

    /**
     * FIFO Queue of operations for passing to the databaseManager
     */
    private Queue<NavEditOperation> dataOperations;

    private MapEditManager() {
        activeMap = null;
        dataOperations = new LinkedList<>();
    }

    /**
     * Sets the map to be edited, should be called upon entering the map edit UI state with a HospitalMap loaded from
     * NavDatabaseManager
     * @param activeMap The instance of map being edited.
     */
    public void setActiveMap(HospitalMap activeMap) {
        this.activeMap = activeMap;
    }

    /**
     * Called to add a entirely new node to the HospitalMap
     * @param node the node to be added, currently requires assigning a uniqueNode ID
     */
    public void addNode(HospitalMapNode node) {
        //TODO set node id using hashing?
        dataOperations.add(new NavEditOperation(ADD_NODE, null, node, null));
        activeMap.getNodes().add(node);
    }

    /**
     * Called when editing a node. Should be passed a new instance of HospitalMapNode representing the edited node
     * Usage, editNode(nodeId, new HospitalMapNode(newInfo)
     * @param nodeId the Id of the node prior to editing
     * @param node A new instance of HospitalMapNode representing the edited node
     */
    public void editNode(String nodeId, HospitalMapNode node) {
        //TODO reset node.id using hashing?
        dataOperations.add(new NavEditOperation(EDIT_NODE, nodeId, node, null));
    }

    /**
     * Called when deleting a node
     * @param nodeId the id of the node to be removed
     */
    public void deleteNode(String nodeId) {
        dataOperations.add(new NavEditOperation(DELETE_NODE, nodeId, null, null));
        activeMap.getNodes().remove(activeMap.getNode(nodeId));
    }

    public void addConnection(String fromNode, String toNode) {
        dataOperations.add(new NavEditOperation(ADD_NODE, fromNode, null, toNode));
        activeMap.getNode(fromNode).getConnections().add(activeMap.getNode(toNode));
        activeMap.getNode(toNode).getConnections().add(activeMap.getNode(fromNode));
    }

    public void removeConnection(String fromNode, String toNode) {
        dataOperations.add(new NavEditOperation(ADD_NODE, fromNode, null, toNode));
        activeMap.getNode(fromNode).getConnections().add(activeMap.getNode(toNode));
        activeMap.getNode(toNode).getConnections().add(activeMap.getNode(fromNode));
    }
}
