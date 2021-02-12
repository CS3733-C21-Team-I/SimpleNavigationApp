package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;

public class NavEditOperation {

    enum OperationType {
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
}
