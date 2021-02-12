package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.util.Queue;

public class MapEditManager {

  private MapEditManager ourInstance;
  private Queue<NavDataOperation> dataOperations;

  private MapEditManager() {
    // TODO - implement MapEditManager.MapEditManager
    throw new UnsupportedOperationException();
  }

  /** @param regen */
  public void init(boolean regen) {
    // TODO - implement MapEditManager.init
    throw new UnsupportedOperationException();
  }

  public MapEditManager getInstance() {
    // TODO - implement MapEditManager.getInstance
    throw new UnsupportedOperationException();
  }

  /** @param node */
  public void addNode(HospitalMapNode node) {
    // TODO - implement MapEditManager.addNode
    throw new UnsupportedOperationException();
  }

  /**
   * @param nodeID
   * @param node
   */
  public void editNode(String nodeID, HospitalMapNode node) {
    // TODO - implement MapEditManager.editNode
    throw new UnsupportedOperationException();
  }

  /** @param nodeID */
  public void deleteNode(String nodeID) {
    // TODO - implement MapEditManager.deleteNode
    throw new UnsupportedOperationException();
  }

  public void saveChanges() {
    // TODO - implement MapEditManager.saveChanges
    throw new UnsupportedOperationException();
  }
}
