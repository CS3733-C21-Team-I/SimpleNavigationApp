package edu.wpi.ithorian.hospitalMap.mapEditing;

import static edu.wpi.ithorian.hospitalMap.mapEditing.NavEditOperation.OperationType.*;

import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.projectCTable.NodeTableController;
import java.io.IOException;
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
  private NodeTableController tableView = null;

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
    dataOperations.add(new NavEditOperation(ADD_NODE, null, node, null));
    activeMap.getNodes().add(node);
  }

  /**
   * Called when editing a node. Should be passed a new instance of HospitalMapNode representing the
   * edited node Usage, editNode(nodeId, new HospitalMapNode(newInfo)
   *
   * @param nodeId the Id of the node prior to editing
   */
  public void editNode(String nodeId) {
    HospitalMapNode node = activeMap.getNode(nodeId);
    // TODO reset node.id using hashing?
    dataOperations.add(new NavEditOperation(EDIT_NODE, nodeId, node, null));
  }

  /**
   * Called when deleting a node
   *
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

  public void startEditorView() throws IOException {
    System.out.println("Starting editor");
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

  public Stage getStage() {
    return stage;
  }
}
