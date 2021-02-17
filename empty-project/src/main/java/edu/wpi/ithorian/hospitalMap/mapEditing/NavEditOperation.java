package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;

public class NavEditOperation {

  public enum OperationType {
    ADD_NODE,
    EDIT_NODE,
    DELETE_NODE,
    ADD_EDGE,
    DELETE_EDGE;
  }

  private OperationType opType;
  private String targetNode;
  private HospitalMapNode newNode;
  private String toNode;

  public NavEditOperation(
      OperationType opType, String targetNode, HospitalMapNode newNode, String toNode) {
    this.opType = opType;
    this.targetNode = targetNode;
    this.newNode = newNode;
    this.toNode = toNode;
  }
}
