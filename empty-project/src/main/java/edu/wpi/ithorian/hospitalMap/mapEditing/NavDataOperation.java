package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;

public class NavDataOperation {

  private OperationType opType;
  private String affectedNode;
  private HospitalMapNode newNode;
  private String toNode;

  public enum OperationType {
    EDIT_NODE,
    ADD_NODE,
    DELETE_NODE,
    ADD_EDGE,
    DELETE_EDGE
  }
}
