package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;

public abstract class MapEditCommand {

  HospitalMap memento;
  MapEditDataController controller;

  public MapEditCommand(MapEditDataController controller) {
    this.controller = controller;
    this.memento = null;
  }

  abstract void execute();

  abstract void unExecute();

  abstract NavEditOperation getOperation();
}

class AddNodeCommand extends MapEditCommand {

  private HospitalMapNode newNode;

  public AddNodeCommand(MapEditDataController controller, HospitalMapNode newNode) {
    super(controller);
    this.newNode = newNode;
  }

  @Override
  void execute() {
    // TODO newNode generateUniqueId
    memento = new HospitalMap(controller.getActiveMap());
    controller.getActiveMap().getNodes().add(newNode);
  }

  @Override
  void unExecute() {
    controller.setActiveMap(memento);
  }

  @Override
  NavEditOperation getOperation() {
    return new NavEditOperation(NavEditOperation.OperationType.ADD_NODE, null, newNode, null);
  }
}

class EditNodeCommand extends MapEditCommand {
  private String nodeEditedId;
  private HospitalMapNode newNode;

  public EditNodeCommand(
      MapEditDataController controller, String nodeEditedId, HospitalMapNode newNode) {
    super(controller);
    this.nodeEditedId = nodeEditedId;
    this.newNode = newNode;
  }

  @Override
  void execute() {
    memento = new HospitalMap(controller.getActiveMap());

    newNode.setId(nodeEditedId);
    //    HospitalMapNode nodeToRemove = null;
    //    for (HospitalMapNode node : controller.getActiveMap().getNodes()) {
    //      if (node.getID().equals(nodeEditedId)) {
    //        nodeToRemove = node;
    //        break;
    //      }
    //    }
    //    if (nodeToRemove == null)
    //      throw new IllegalArgumentException("Node to edit was not included within activeMap");

    for (HospitalMapNode node : controller.getActiveMap().getNodes()) {
      boolean connected = false;
      for (HospitalMapNode connection : node.getConnections()) {
        if (connection.getID() == nodeEditedId) {
          connected = true;
          break;
        }
      }
      if (connected) {
        node.getConnections().remove(controller.getActiveMap().getNode(nodeEditedId));
        node.getConnections().add(newNode);
      }
    }

    controller.getActiveMap().removeNode(nodeEditedId);

    controller.getActiveMap().getNodes().add(newNode);
  }

  @Override
  void unExecute() {
    controller.setActiveMap(memento);
  }

  @Override
  NavEditOperation getOperation() {
    return new NavEditOperation(
        NavEditOperation.OperationType.EDIT_NODE, nodeEditedId, newNode, null);
  }
}

class RemoveNodeCommand extends MapEditCommand {

  private String nodeIdToRemove;

  public RemoveNodeCommand(MapEditDataController controller, String nodeIdToRemove) {
    super(controller);
    this.nodeIdToRemove = nodeIdToRemove;
  }

  @Override
  void execute() {
    memento = new HospitalMap(controller.getActiveMap());

    controller.getActiveMap().removeNode(nodeIdToRemove);
  }

  @Override
  void unExecute() {
    controller.setActiveMap(memento);
  }

  @Override
  NavEditOperation getOperation() {
    return new NavEditOperation(
        NavEditOperation.OperationType.DELETE_NODE, nodeIdToRemove, null, null);
  }
}

class AddEdgeCommand extends MapEditCommand {
  private HospitalMapNode fromNode;
  private HospitalMapNode toNode;

  public AddEdgeCommand(
      MapEditDataController controller, HospitalMapNode fromNode, HospitalMapNode toNode) {
    super(controller);
    this.fromNode = fromNode;
    this.toNode = toNode;
  }

  @Override
  void execute() {
    memento = new HospitalMap(controller.getActiveMap());
    // fromNode.addConnection(toNode);
    // toNode.addConnection(fromNode);
    controller.getActiveMap().getNode(fromNode.getID()).addConnection(toNode);
    controller.getActiveMap().getNode(toNode.getID()).addConnection(fromNode);
    System.out.println("Fromnodeconnections:" + fromNode.getConnections());
    System.out.println("Fromnodeconnections:" + fromNode.getConnections());
  }

  @Override
  void unExecute() {
    controller.setActiveMap(memento);
  }

  @Override
  NavEditOperation getOperation() {
    return new NavEditOperation(
        NavEditOperation.OperationType.ADD_EDGE, fromNode.getID(), null, toNode.getID());
  }
}

class DeleteEdgeCommand extends MapEditCommand {
  private HospitalMapNode fromNode;
  private HospitalMapNode toNode;

  public DeleteEdgeCommand(
      MapEditDataController controller, HospitalMapNode fromNode, HospitalMapNode toNode) {
    super(controller);
    this.fromNode = fromNode;
    this.toNode = toNode;
  }

  @Override
  void execute() {
    memento = new HospitalMap(controller.getActiveMap());

    controller
        .getActiveMap()
        .getNode(fromNode.getID())
        .getConnections()
        .removeIf(n -> n.getID().equals(toNode.getID()));
    controller
        .getActiveMap()
        .getNode(toNode.getID())
        .getConnections()
        .removeIf(n -> n.getID().equals(fromNode.getID()));
  }

  @Override
  void unExecute() {
    controller.setActiveMap(memento);
  }

  @Override
  NavEditOperation getOperation() {
    return new NavEditOperation(
        NavEditOperation.OperationType.DELETE_EDGE, fromNode.getID(), null, toNode.getID());
  }
}
