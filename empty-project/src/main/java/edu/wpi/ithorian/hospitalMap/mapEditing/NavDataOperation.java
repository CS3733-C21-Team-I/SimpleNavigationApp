package edu.wpi.ithorian.hospitalMap.mapEditing;

public class NavDataOperation {

	private OperationType opType;
	private String affectedNode;
	private hospitalMap.HospitalMapNode newNode;
	private String toNode;


	public enum OperationType {
		EDIT_NODE,
		ADD_NODE,
		DELETE_NODE,
		ADD_EDGE,
		DELETE_EDGE
	}

}