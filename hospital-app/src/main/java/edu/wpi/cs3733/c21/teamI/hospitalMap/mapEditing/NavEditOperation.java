package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;

public class NavEditOperation {

  enum OperationType {
    ADD_NODE,
    EDIT_NODE,
    DELETE_NODE,
    ADD_EDGE,
    DELETE_EDGE
  }

  private final OperationType opType;
  private final String targetNode;
  private final HospitalMapNode newNode;
  private final String toNode;

  public NavEditOperation(
      OperationType opType, String targetNode, HospitalMapNode newNode, String toNode) {
    this.opType = opType;
    this.targetNode = targetNode;
    this.newNode = newNode;
    this.toNode = toNode;
  }
}
