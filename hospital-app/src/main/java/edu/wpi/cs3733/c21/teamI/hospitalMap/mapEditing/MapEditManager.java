package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javafx.scene.Group;
import javafx.stage.Stage;

public class MapEditManager {

  private static MapEditManager ourInstance;
  private int scale = 3; // scales image to 1/scale
  private MapEditView mapEditorView = null;
  private ApplicationView applicationView = null;

  /**
   * Represents the map that is activelyBeing edited should be referenced from this class's getter
   * for all UI calls
   */
  private HospitalMap activeMap;

  private Group root = null;
  private Stage stage = null;

  public static void init() {
    ourInstance = new MapEditManager();
  }

  public static MapEditManager getInstance() {
    return ourInstance;
  }

  /** FIFO Queue of operations for passing to the databaseManager */
  private Queue<NavEditOperation> dataOperations;

  public MapEditManager() {
    activeMap = null;
    dataOperations = new LinkedList<>();
  }

  /**
   * Sets the map to be edited, should be called upon entering the map edit UI state with a
   * HospitalMap loaded from NavDatabaseManager
   *
   * @param activeMap The instance of map being edited.
   */
  public void setActiveMap(HospitalMap activeMap) {
    this.activeMap = activeMap;
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

  /**
   * takes in a node object and assigns / de-assigns it to the selectedNode variable in MapState
   *
   * @param node the node to be assigned or de-assigned
   */
  public void toggleNode(HospitalMapNode node) {
    if (activeMap.getSelectedNode() == null) {
      activeMap.setSelectedNode(node);
    } else if (activeMap.getSelectedNode().equals(node)) {
      activeMap.setSelectedNode(null);
    } else {
      makeEdge();
      activeMap.setSelectedNode(null);
    }
  }

  private void makeEdge() {
    // TODO: Implement making an edge
  }

  public void addConnection(String fromNode, String toNode) {
    dataOperations.add(
        new NavEditOperation(NavEditOperation.OperationType.ADD_NODE, fromNode, null, toNode));
    activeMap.getNode(fromNode).getConnections().add(activeMap.getNode(toNode));
    activeMap.getNode(toNode).getConnections().add(activeMap.getNode(fromNode));
  }

  public void removeConnection(String fromNode, String toNode) {
    dataOperations.add(
        new NavEditOperation(NavEditOperation.OperationType.ADD_NODE, fromNode, null, toNode));
    activeMap.getNode(fromNode).getConnections().add(activeMap.getNode(toNode));
    activeMap.getNode(toNode).getConnections().add(activeMap.getNode(fromNode));
  }

  public void saveChanges() {
    // TODO send to Database
    activeMap = null;
  }

  public int getScale() {
    return scale;
  }

  public Set<HospitalMapNode> getEntityNodes() {
    return activeMap.getNodes();
  }

  public String getImagePath() {
    return activeMap.getImagePath();
  }

  public void startEditorView() {
    mapEditorView = new MapEditView(this);
    mapEditorView.start(stage);
    mapEditorView.saveManager();
  }

  public void startApplicationView() {
    this.root = new Group();
    applicationView = new ApplicationView(this);
    applicationView.saveManager();
  }

  public void setRoot(Group root) {
    this.root = root;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Group getRoot() {
    return root;
  }

  public HospitalMapNode getSelectedNode() {
    return activeMap.getSelectedNode();
  }
}