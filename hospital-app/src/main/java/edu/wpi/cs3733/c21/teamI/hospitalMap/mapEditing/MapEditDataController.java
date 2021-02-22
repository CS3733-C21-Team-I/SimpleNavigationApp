package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MapEditDataController {

  /**
   * FIFO Stack of MapEdit Commands implementing the command pattern with a stored momento Used for
   * undo redo operations
   */
  private Stack<MapEditCommand> dataOperations;

  private int undoRedoPointer;

  /**
   * Represents the map that is activelyBeing edited should be referenced from this class's getter
   * for all UI calls
   */
  private HospitalMap activeMap;

  MapEditDataController() {
    activeMap = null;
    dataOperations = new Stack<>();
    undoRedoPointer = -1;
  }

  /**
   * Called to add a entirely new node to the HospitalMap
   *
   * @param node the node to be added, currently requires assigning a uniqueNode ID
   */
  public void addNode(HospitalMapNode node) {
    deleteElementsAfterPointer(undoRedoPointer);
    MapEditCommand command = new AddNodeCommand(this, node);
    command.execute();
    dataOperations.push(command);
    undoRedoPointer++;
  }

  /**
   * Called when editing a node. Should be passed a new instance of HospitalMapNode representing the
   * edited node Usage, editNode(nodeId, new HospitalMapNode(newInfo)
   *
   * @param nodeId the Id of the node prior to editing
   */
  public void editNode(String nodeId, HospitalMapNode newNode) {
    deleteElementsAfterPointer(undoRedoPointer);
    MapEditCommand command = new EditNodeCommand(this, nodeId, newNode);
    command.execute();
    dataOperations.push(command);
    undoRedoPointer++;
  }

  /**
   * Called when deleting a node
   *
   * @param nodeId the id of the node to be removed
   */
  public void deleteNode(String nodeId) {
    deleteElementsAfterPointer(undoRedoPointer);
    MapEditCommand command = new RemoveNodeCommand(this, nodeId);
    command.execute();
    dataOperations.push(command);
    undoRedoPointer++;
  }

  public void addEdge(String fromNode, String toNode) {
    deleteElementsAfterPointer(undoRedoPointer);
    MapEditCommand command = new AddEdgeCommand(this, fromNode, toNode);
    command.execute();
    dataOperations.push(command);
    undoRedoPointer++;
  }

  public void deleteEdge(String fromNode, String toNode) {
    deleteElementsAfterPointer(undoRedoPointer);
    MapEditCommand command = new DeleteEdgeCommand(this, fromNode, toNode);
    command.execute();
    dataOperations.push(command);
    undoRedoPointer++;
  }

  public HospitalMap getActiveMap() {
    return activeMap;
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
   * Called when done editing a single map, will commit the map into the database and sets activeMap
   * to none
   */
  public void saveChanges() {
    // TODO send to Database
    Queue<NavEditOperation> ops = new LinkedList<>();
    if (undoRedoPointer != -1) {
      for (int i = 0; i < undoRedoPointer; i++) {
        ops.add(dataOperations.get(i).getOperation());
      }
    }

    NavDatabaseManager.getInstance().applyNavEditOperations(ops);

    activeMap = null;
    dataOperations = new Stack<>();
    undoRedoPointer = -1;
  }

  public void discardChanges() {
    activeMap = null;
    dataOperations = new Stack<>();
    undoRedoPointer = -1;
  }

  public void undo() {
    MapEditCommand command = dataOperations.get(undoRedoPointer);
    command.unExecute();
    undoRedoPointer--;
  }

  public boolean isUndoAvailable() {
    return undoRedoPointer >= -1;
  }

  public boolean isRedoAvailable() {
    return undoRedoPointer == dataOperations.size() - 1;
  }

  public void redo() {
    if (undoRedoPointer == dataOperations.size() - 1) return;
    undoRedoPointer++;
    MapEditCommand command = dataOperations.get(undoRedoPointer);
    command.execute();
  }

  private void deleteElementsAfterPointer(int undoRedoPointer) {
    if (dataOperations.size() < 1) return;
    for (int i = dataOperations.size() - 1; i > undoRedoPointer; i--) {
      dataOperations.remove(i);
    }
  }
}
